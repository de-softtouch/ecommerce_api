package com.api.ecommerceweb.service;

import com.api.ecommerceweb.dto.AddressDTO;
import com.api.ecommerceweb.enumm.ERole;
import com.api.ecommerceweb.mapper.AddressMapper;
import com.api.ecommerceweb.model.*;
import com.api.ecommerceweb.reponse.BaseResponseBody;
import com.api.ecommerceweb.reponse.BasicUserInfoResponse;
import com.api.ecommerceweb.reponse.FeedbackResponse;
import com.api.ecommerceweb.repository.*;
import com.api.ecommerceweb.request.*;
import com.api.ecommerceweb.security.CustomUserDetails;
import com.api.ecommerceweb.security.JwtTokenUtil;
import com.api.ecommerceweb.utils.MailUtil;
import com.api.ecommerceweb.utils.ValidationUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepo;
    private final MailUtil mailUtil;
    private final PasswordEncoder passwordEncoder;
    private final AddressRepository addressRepo;
    private final OrderRepository orderRepo;
    private final FeedbackRepository feedbackRepo;
    private final ProductRepository productRepo;
    private final ShopRepository shopRepo;
    private final OrderItemRepository orderItemRepo;
    private final RoleRepository roleRepo;
    private final JwtTokenUtil jwtTokenUtil;
    private final ModelMapper modelMapper;

    public User getCurrentUser() {
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return getById(userDetails.getId());
    }

    public BasicUserInfoResponse getCurrentUserDetail() {
        User user = getCurrentUser();
        BasicUserInfoResponse userDetail = modelMapper.map(user, BasicUserInfoResponse.class);
        return userDetail;
    }

    public ResponseEntity<?> updateCurrentUserDetails(AccountUpdateRequest accountUpdateRequest, MultipartFile multipartFile) {
        Map<String, Object> map = new HashMap<>();
        //get current user details
        User user = getCurrentUser();
        user.setFullName(accountUpdateRequest.getFullName());
        user.setGender(accountUpdateRequest.getGender());
        map.put("fullName", accountUpdateRequest.getFullName());
        map.put("gender", accountUpdateRequest.getGender());
        //check file is image
//        TODO:update user avt
//        if (multipartFile != null && !multipartFile.isEmpty()) {
//            boolean image = fileStorageUtil.isImage(multipartFile);
//            if (!image)
//                return ResponseEntity.badRequest().body("File is not image type");
//            //save file
//            String folder = user.getId().toString();
//            String savedFileName = fileStorageUtil.storeFile(multipartFile, folder);
//            map.put("profileImg", savedFileName);
//            user.setProfileImg(savedFileName);
//        }
        userRepo.save(user);
        return ResponseEntity.ok(map);
    }

    public Shop getCurrentUserShop() {
        User currentUser = getCurrentUser();
        Optional<Shop> optional = shopRepo.findByOwners(currentUser);
        if (optional.isPresent()) {
            return optional.get();
        }
        return null;
    }

    public boolean isExistedEmail(String email) {
        return userRepo.existsByEmail(email);
    }

    public boolean isExistedPhone(String phone) {
        return userRepo.existsByPhone(phone);
    }

    public User findUserByCode(String code) {
        Optional<User> optionalUser = userRepo.findByVerificationCode(code);
        if (optionalUser.isPresent()) {
            return optionalUser.get();
        }
        return null;
    }

    public User save(User user) {
        return userRepo.save(user);
    }

    public Role getMemberRole() {
        Role role = roleRepo.getByName(ERole.ROLE_MEMBER);
        return role;
    }

    public Role getSellerRole() {
        return roleRepo.getByName(ERole.ROLE_SELLER);
    }

    public Map<String, Object> toBasicUserInfo(User user) {
        Map<String, Object> userInfoResp = new HashMap<>();
        userInfoResp.put("id", user.getId());
        userInfoResp.put("fullName", user.getFullName());
        userInfoResp.put("email", user.getEmail());
        userInfoResp.put("phone", user.getPhone());
        userInfoResp.put("avt", user.getProfileImg());
        userInfoResp.put("access_token", jwtTokenUtil.generateAccessToken(user));
        userInfoResp.put("refresh_token", jwtTokenUtil.generateRefreshToken(user));
        if (user.getShop() != null) {
            userInfoResp.put("shopId", user.getShop().getId());
            userInfoResp.put("type", "seller");
        }
        return userInfoResp;
    }


    public Address saveAddress(AddressRequest addressRequest) {
        Address address;
        if (addressRequest.getId() != null && addressRepo.existsById(addressRequest.getId())) {
            address = addressRepo.getById(addressRequest.getId());
        } else {
            address = new Address();
        }
        address.setPhone(addressRequest.getPhone());
        address.setFullName(addressRequest.getFullName());
        address.setAddressDetails(addressRequest.getAddressDetails());
        address.setPostCode(addressRequest.getPostCode());
        address.setUser(getCurrentUser());
        address = addressRepo.save(address);
        return address;
    }


    public boolean existById(Long idRequest) {
        return userRepo.existsById(idRequest);
    }

    public User getById(Long idRequest) {
        return userRepo.getById(idRequest);

    }

    public void saveShop(Shop shop) {
        shop.getOwners().add(getCurrentUser());
        shop = shopRepo.save(shop);
        User currentUser = getCurrentUser();
        currentUser.setShop(shop);
        currentUser.getRoles().add(getSellerRole());
        userRepo.save(currentUser);
    }

    public boolean activateShop(String code) {
        User currentUser = getCurrentUser();
        Optional<Shop> optionalShop = shopRepo.findByOwners(currentUser);
        if (optionalShop.isEmpty()) {
            return false;
        }
        Shop shop = optionalShop.get();
        if (code.length() == 6 && shop.getVerificationCode().equals(code)) {
            shop.setVerificationCode(null);
            shop.setActive(1);
            shopRepo.save(shop);
            return true;
        }
        return false;
    }


    public ResponseEntity<?> getVerificationCode() {
        User user = getCurrentUser();
        try {
            String verificationCode = mailUtil.generateVerificationCode();
            mailUtil.sendVerificationCode(user.getUsername(), verificationCode);
            user.setVerificationCode(verificationCode);
            userRepo.save(user);
            return ResponseEntity.ok("Verification code was sent");
        } catch (MessagingException | UnsupportedEncodingException e) {
            e.printStackTrace();
            log.error("Send code error - {}", e.getMessage());
        }
        return ResponseEntity.internalServerError().build();
    }

    public ResponseEntity<?> resetPassword(ResetPasswordRequest resetPasswordRequest) {
        User user = getCurrentUser();

        if (!passwordEncoder.matches(resetPasswordRequest.getOldPassword(), user.getPassword()))
            return ResponseEntity.badRequest().body("Password does not match old password!");

        if (!resetPasswordRequest.getVerificationCode().equals(user.getVerificationCode()))
            return ResponseEntity.badRequest().body("Verification code does not match!");
        user.setVerificationCode(null);
        user.setPassword(passwordEncoder.encode(resetPasswordRequest.getNewPassword()));
        userRepo.save(user);
        return ResponseEntity.ok("Reset password success");
    }

    public ResponseEntity<?> getAddresses() {
        User user = getCurrentUser();
        List<Address> addresses = addressRepo.findByUserAndStatus(user, 1);
        List<AddressDTO> rs = addresses.stream()
                .map(AddressMapper::toAddressDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(rs);
    }

    public ResponseEntity<?> updateAddress(AddressRequest addressRequest) {
        User user = getCurrentUser();
        List<Address> savedAddresses = addressRepo.findByUserAndStatus(user, 1);
        Address address;
        if (addressRequest.getId() != null
                && addressRepo.existsById(addressRequest.getId())) {
            address = addressRepo.getById(addressRequest.getId());
            if (savedAddresses.size() == 1)
                address.setIsDefault(1);
        } else {
            address = new Address();
            address.setUser(user);
        }
        address.setFullName(addressRequest.getFullName());
        address.setPhone(addressRequest.getPhone());
        address.setType(addressRequest.getType());
        address.setAddressDetails(addressRequest.getAddressDetails());
        address.setPostCode(addressRequest.getPostCode());
        if (addressRequest.getIsDefault() != null && addressRequest.getIsDefault() == 1) {
            savedAddresses
                    .forEach(a -> {
                        a.setIsDefault(0);
                        addressRepo.save(a);
                    });
            address.setIsDefault(1);
        } else if (savedAddresses.size() == 0) {
            address.setIsDefault(1);
        }
        Address data = addressRepo.saveAndFlush(address);
        return ResponseEntity.ok(BaseResponseBody.success(modelMapper.map(data, AddressDTO.class), "update address", "success"));
    }

    public ResponseEntity<?> deleteAddress(long id) {
        Optional<Address> optionalAddress = addressRepo.findById(id);
        if (optionalAddress.isPresent()) {
            Address address = optionalAddress.get();
            address.setStatus(0);
            addressRepo.save(address);
            return ResponseEntity.ok("Delete address success");
        }
        return ResponseEntity.badRequest().body("Can not find address with id: " + id);
    }

    public ResponseEntity<?> getOrders(Map<String, String> params) {
        BaseParamRequest p = new BaseParamRequest(params);

        Pageable pageable = p.toPageRequest();
        if (ValidationUtil.isNullOrBlank(params.get("status"))) {
            p.setStatus(null);
        }
//        Sort sort = Sort.by(Sort.Direction.fromString(p.getDirection()), p.getSortBy(),"status");
        List<Order> orders = orderRepo.getOrdersNative(p.getStatus(), pageable);
        List<Object> rs = new ArrayList<>();
        for (Order o : orders) {
            Map<String, Object> orderResponse = toOrderDetailsResponse(o);
            rs.add(orderResponse);
        }
        return ResponseEntity.ok(rs);
    }

    private Map<String, Object> toOrderDetailsResponse(Order order) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", order.getId());
        map.put("orderDate", order.getCreateDate());
        map.put("status", order.getStatus());
        //user
        User orderUser = order.getUser();
        Map<String, Object> u = new HashMap<>();
        u.put("id", orderUser.getId());
        u.put("username", orderUser.getFullName());
        //address
        Address address = order.getAddress();
        Map<String, Object> a = new HashMap<>();
        a.put("id", address.getId());
        a.put("addressDetails", address.getAddressDetails());
        a.put("type", address.getType());
        a.put("postCode", address.getPostCode());
        a.put("phone", address.getPhone());
        u.put("address", a);
        map.put("user", u);

        //items
        List<Object> orderItems = new ArrayList<>();
        for (OrderItem item :
                order.getItems()) {
            Map<String, Object> o = new HashMap<>();
            o.put("qty", item.getQty());
            o.put("message", item.getMessage());
            o.put("productId", item.getProduct().getId());
            o.put("name", item.getProduct().getName());
            orderItems.add(o);
        }
        map.put("orderItems", orderItems);
        return map;
    }

    public ResponseEntity<?> cancelOrder(Long id) {
//        Optional<Order> optionalOrder = orderRepo.findByIdAndUser(id, getCurrentUser());
//        if (optionalOrder.isPresent()) {
//            Order order = optionalOrder.get();
//            if (!order.getStatus().equals(OrderStatus.TO_RECEIVE) && !order.getStatus().equals(OrderStatus.COMPLETED)) {
//                order.setStatus(OrderStatus.CANCELLED);
//                orderRepo.save(order);
//                //send email to seller user canceled
//                List<Shop> shops = order.getShops();
//                for (Shop sh :
//                        shops) {
//                    for (User owner :
//                            sh.getOwners()) {
//                        try {
//                            mailUtil.sendCancelOrderInfo(owner.getEmail(), String.valueOf(order.getId()), order.getUser().getFullName(), new Date(), "...reason..");
//                        } catch (MessagingException | UnsupportedEncodingException e) {
//                            e.printStackTrace();
//                            log.error("Can not send cancel order mail to shop owner - {}", e.getMessage());
//                        }
//                    }
//                }
//                return ResponseEntity.ok("Canceled order #" + id);
//            } else {
//                return ResponseEntity.badRequest().body("Can not cancel because the order was shipping");
//            }
//        }
//        return ResponseEntity.notFound().build();
        return null;
    }

    public ResponseEntity<?> getFeedBack() {
        List<Feedback> feedbacks = feedbackRepo.findAllByUser(getCurrentUser());
        List<Object> rs = new ArrayList<>();
        for (Feedback feedback :
                feedbacks) {
            Map<String, Object> feedbackResponse = new HashMap<>();
            feedbackResponse.put("id", feedback.getId());
            feedbackResponse.put("comment", feedback.getComment());
            feedbackResponse.put("rating", feedback.getRating());
            feedbackResponse.put("createDate", feedback.getCreateDate());
            rs.add(feedbackResponse);
        }
        return ResponseEntity.ok(rs);
    }

    public ResponseEntity<?> postFeedback(FeedbackRequest request) {
        //make sure user already have bought product
//        Optional<Order> optionalOrder = orderRepo.findByIdAndUser(request.getProductId(), getCurrentUser());
//        if (optionalOrder.isEmpty()) {
//            return ResponseEntity.badRequest().body("User have not bought product yet");
//        }
        Optional<Product> optionalProduct = productRepo.findById(request.getProductId());
        if (optionalProduct.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Feedback feedback;
        if (request.getId() != null && feedbackRepo.existsById(request.getId())) {
            feedback = feedbackRepo.getById(request.getId());
        } else {
            feedback = new Feedback();
            feedback.setProduct(optionalProduct.get());
            feedback.setStatus(1);
            feedback.setUser(getCurrentUser());
        }
        feedback.setComment(request.getComment());
        feedback.setRating(request.getRating());
        feedback = feedbackRepo.saveAndFlush(feedback);
        FeedbackResponse data = modelMapper.map(feedback, FeedbackResponse.class);
        return ResponseEntity.ok(BaseResponseBody.success(data, "save feedback success", null));
    }


    public ResponseEntity<?> orders(OrderRequest request) {
        Order order = new Order();
        //order user
        order.setUser(getCurrentUser());
        //shop
        order = orderRepo.save(order);
        //order items
        for (OrderItemRequest itemRequest :
                request.getItems()) {
            OrderItem orderItem = new OrderItem();
            orderItem.setMessage(itemRequest.getMessage());
            orderItem.setQty(itemRequest.getQty());
            //set shop

            //set address
            //set product
            Optional<Product> optionalProduct = productRepo.findById(itemRequest.getProductId());
            if (optionalProduct.isEmpty())
                return ResponseEntity.badRequest().body("address is not exist");
            orderItem.setProduct(optionalProduct.get());
            //variant

            order.getItems().add(orderItem);
            orderItem.setOrder(order);
            orderItemRepo.save(orderItem);
            //
        }
        return ResponseEntity.ok("order success");
    }


    public boolean isExistedUsername(String value) {
        return userRepo.existsByUsername(value);
    }


}
