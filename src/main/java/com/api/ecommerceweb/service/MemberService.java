package com.api.ecommerceweb.service;

import com.api.ecommerceweb.request.ResetPasswordRequest;
import com.api.ecommerceweb.dto.AddressDTO;
import com.api.ecommerceweb.enumm.ERole;
import com.api.ecommerceweb.mapper.AddressMapper;
import com.api.ecommerceweb.model.*;
import com.api.ecommerceweb.repository.*;
import com.api.ecommerceweb.request.*;
import com.api.ecommerceweb.security.CustomUserDetails;
import com.api.ecommerceweb.security.JwtTokenUtil;
import com.api.ecommerceweb.utils.FileStorageUtil;
import com.api.ecommerceweb.utils.MailUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.*;
import java.util.stream.Collectors;


@Component("MemberHelper")
@RequiredArgsConstructor
@Slf4j
public class MemberService {

    private final UserRepository userRepo;


    private final FileStorageUtil fileStorageUtil;
    private final MailUtil mailUtil;
    private final PasswordEncoder passwordEncoder;
    private final AddressRepository addressRepo;
    private final OrderRepository orderRepo;
    private final FeedbackRepository feedbackRepo;
    private final ProductRepository productRepo;
    private final ShopRepository shopRepo;
    private final VariationRepository variantRepo;
    private final OrderItemRepository orderItemRepo;
    private final RoleRepository roleRepo;
    private final JwtTokenUtil jwtTokenUtil;


    public ResponseEntity<?> getCurrentUserDetails() {
        CustomUserDetails principal = getPrincipal();
//        UserDTO dto = UserMapper.toUserDto(principal.getUser());
        Map<String, Object> resp = toBasicUserInfo(principal.getUser());
        return ResponseEntity.ok(resp);
    }

    public ResponseEntity<?> updateCurrentUserDetails(AccountUpdateRequest accountUpdateRequest, MultipartFile multipartFile) {
        Map<String, Object> map = new HashMap<>();
        //get current user details
        CustomUserDetails principal = getPrincipal();
        User user = principal.getUser();
        user.setFullName(accountUpdateRequest.getFullName());
        user.setGender(accountUpdateRequest.getGender());
        map.put("fullName", accountUpdateRequest.getFullName());
        map.put("gender", accountUpdateRequest.getGender());
        //check file is image
        if (multipartFile != null && !multipartFile.isEmpty()) {
            boolean image = fileStorageUtil.isImage(multipartFile);
            if (!image)
                return ResponseEntity.badRequest().body("File is not image type");
            //save file
            String folder = user.getId().toString();
            String savedFileName = fileStorageUtil.storeFile(multipartFile, folder);
            map.put("profileImg", savedFileName);
            user.setProfileImg(savedFileName);
        }
        userRepo.save(user);
        return ResponseEntity.ok(map);
    }

    public ResponseEntity<?> getFiles() {
        CustomUserDetails principal = getPrincipal();
        List<File> files = fileStorageUtil.getFiles(principal.getUser().getId().toString());
        return ResponseEntity.ok(
                files.stream().map(File::getName).collect(Collectors.toList())
        );
    }

    public ResponseEntity<?> getVerificationCode() {
        CustomUserDetails principal = getPrincipal();
        User user = principal.getUser();
        try {
            String verificationCode = mailUtil.generateVerificationCode();
            mailUtil.sendVerificationCode(principal.getUsername(), verificationCode);
            user.setVerificationCode(verificationCode);
            userRepo.save(user);
            return ResponseEntity.ok("Verification code was sent");
        } catch (MessagingException | UnsupportedEncodingException e) {
            e.printStackTrace();
            log.error("Send code error - {}", e.getMessage());
        }
        return ResponseEntity.internalServerError().build();
    }

    private CustomUserDetails getPrincipal() {
        return (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    public ResponseEntity<?> resetPassword(ResetPasswordRequest resetPasswordRequest) {
        CustomUserDetails principal = getPrincipal();


        if (!passwordEncoder.matches(resetPasswordRequest.getOldPassword(), principal.getPassword()))
            return ResponseEntity.badRequest().body("Password does not match old password!");

        if (!resetPasswordRequest.getVerificationCode().equals(principal.getUser().getVerificationCode()))
            return ResponseEntity.badRequest().body("Verification code does not match!");

        User user = principal.getUser();
        user.setVerificationCode(null);
        user.setPassword(passwordEncoder.encode(resetPasswordRequest.getNewPassword()));
        userRepo.save(user);
        return ResponseEntity.ok("Reset password success");
    }

    public ResponseEntity<?> getAddresses() {
        CustomUserDetails principal = getPrincipal();
        List<Address> addresses = addressRepo.findByUserAndStatus(principal.getUser(), 1);
        List<AddressDTO> rs = addresses.stream()
                .map(AddressMapper::toAddressDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(rs);
    }

    public ResponseEntity<?> updateAddress(AddressRequest addressRequest) {
        List<Address> savedAddresses = addressRepo.findByUserAndStatus(getPrincipal().getUser(), 1);

        Address address;
        if (addressRequest.getId() != null
                && addressRepo.existsById(addressRequest.getId())) {
            address = addressRepo.getById(addressRequest.getId());
            if (savedAddresses.size() == 1)
                address.setIsDefault(1);
        } else {
            address = new Address();
            address.setUser(getPrincipal().getUser());
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
        addressRepo.save(address);
        return ResponseEntity.ok("Update address success");
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

    public ResponseEntity<?> getOrders() {
        List<Order> orders = orderRepo.findAllByUser(getCurrentUser());
        List<Object> rs = new ArrayList<>();
        for (Order o : orders) {
            Map<String, Object> orderResponse = toOrderDetailsResponse(o);
            rs.add(orderResponse);
        }
        return ResponseEntity.ok(rs);
    }

    private User getCurrentUser() {
        CustomUserDetails principal = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return principal.getUser();
    }

    private Map<String, Object> toOrderDetailsResponse(Order order) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", order.getId());
        map.put("orderDate", order.getOrderDate());
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
                order.getOrderItems()) {
            Map<String, Object> o = new HashMap<>();
            o.put("qty", item.getQty());
            o.put("message", item.getMessage());
            o.put("productId", item.getProduct().getId());
            o.put("name", item.getProduct().getName());
            o.put("size", item.getVariation().getSize().getSize());
            o.put("color", item.getVariation().getColor().getCode());
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

    private Map<String, Object> toBasicUserInfo(User user) {
        Map<String, Object> userInfoResp = new HashMap<>();
        userInfoResp.put("id", user.getId());
        userInfoResp.put("fullName", user.getFullName());
        userInfoResp.put("email", user.getEmail());
        userInfoResp.put("phone", user.getPhone());
        userInfoResp.put("avt", user.getProfileImg());
        if (user.getShop() != null) {
            userInfoResp.put("shopId", user.getShop().getId());
            userInfoResp.put("type", "seller");
        }
        return userInfoResp;
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
        Optional<Order> optionalOrder = orderRepo.findByIdAndUser(request.getProductId(), getCurrentUser());
        if (optionalOrder.isEmpty()) {
            return ResponseEntity.badRequest().body("User have not bought product yet");
        }
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
        }
        feedback.setComment(request.getComment());
        feedback.setRating(request.getRating());
        feedbackRepo.save(feedback);
        return ResponseEntity.ok("Save feedback success");
    }


    public ResponseEntity<?> orders(OrderRequest request) {
        Order order = new Order();
        //order user
        order.setUser(getCurrentUser());
        //shop
        Optional<Shop> optionalShop = shopRepo.findById(request.getShopId());
        if (optionalShop.isEmpty())
            return ResponseEntity.badRequest().body("shop is not exist");
//        order.setShop(optionalShop.get());
        order = orderRepo.save(order);
        //order items
        for (OrderItemRequest itemRequest :
                request.getItems()) {
            OrderItem orderItem = new OrderItem();
            orderItem.setMessage(itemRequest.getMessage());
            orderItem.setQty(itemRequest.getQty());
            //set shop

            //set address
            Optional<Address> optionalAddress = addressRepo.findById(itemRequest.getAddressId());
            if (optionalAddress.isEmpty())
                return ResponseEntity.badRequest().body("address is not exist");
            orderItem.setAddress(optionalAddress.get());
            //set product
            Optional<Product> optionalProduct = productRepo.findById(itemRequest.getProductId());
            if (optionalProduct.isEmpty())
                return ResponseEntity.badRequest().body("address is not exist");
            orderItem.setProduct(optionalProduct.get());
            //variant
            Optional<Variation> optionalVariation = variantRepo.findById(itemRequest.getVariantId());
            if (optionalVariation.isEmpty())
                return ResponseEntity.badRequest().body("variation is not exist");
            orderItem.setVariation(optionalVariation.get());
            order.getOrderItems().add(orderItem);
            orderItem.setOrder(order);
            orderItemRepo.save(orderItem);
            //
        }
        return ResponseEntity.ok("order success");
    }


    public Shop saveShop(ShopRequest shopRequest) {
        Long shopRequestId = shopRequest.getId();
        User user = getCurrentUser();
        Shop shop;
        if (shopRequestId != null && shopRepo.existsById(shopRequestId)) {
            shop = shopRepo.getById(shopRequestId);
        } else {
            shop = new Shop();
            user.getRoles().add(roleRepo.getByName(ERole.ROLE_SELLER));
        }
        if (shopRequest.getAddress() != null) {
            Address address = saveAddress(shopRequest.getAddress());
            address.setShop(shop);
            shop.setAddress(address);
        }
        shop.setName(shopRequest.getName());
        shop.setDescription(shopRequest.getDescription());
        shop.getOwners().add(user);
        shop = shopRepo.save(shop);
        user.setShop(shop);
        userRepo.save(user);
        return shop;
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

}