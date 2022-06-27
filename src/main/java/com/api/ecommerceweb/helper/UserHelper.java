package com.api.ecommerceweb.helper;

import com.api.ecommerceweb.model.Shop;
import com.api.ecommerceweb.model.User;
import com.api.ecommerceweb.reponse.BaseResponseBody;
import com.api.ecommerceweb.reponse.BasicUserInfoResponse;
import com.api.ecommerceweb.request.*;
import com.api.ecommerceweb.security.JwtTokenUtil;
import com.api.ecommerceweb.service.MailService;
import com.api.ecommerceweb.service.UserService;
import com.api.ecommerceweb.utils.ValidationUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;
import java.util.Map;

@Component("UserHelper")
@RequiredArgsConstructor
@Slf4j
public class UserHelper {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final MailService mailService;
    private final ModelMapper modelMapper;
    private final JwtTokenUtil jwtTokenUtil;

    public ResponseEntity<?> validationInputField(String input, String value) {
        if (input == null || value == null || input.trim().isBlank() || value.trim().isBlank())
            return ResponseEntity.badRequest().build();
        input = input.trim();
        value = value.trim();
        BaseResponseBody resp;
        if (input.equalsIgnoreCase("email")) {
            if (!ValidationUtil.isEmail(value)) {
                resp = BaseResponseBody.badRequest("Email is not valid!");
                return ResponseEntity.badRequest().body(resp);
            }
            if (userService.isExistedEmail(value)) {
                resp = BaseResponseBody.resourceExist("Email has been used!");
                return ResponseEntity.status(resp.getStatusCode()).body(resp);
            }
        } else if (input.equalsIgnoreCase("phone")) {
            if (!ValidationUtil.isPhoneNumber(value)) {
                resp = BaseResponseBody.badRequest("Phone is not valid!");
                return ResponseEntity.badRequest().body(resp);
            }
            if (userService.isExistedPhone(value)) {
                resp = BaseResponseBody.resourceExist("Phone has been used!");
                return ResponseEntity.status(resp.getStatusCode()).body(resp);
            }
        } else if (input.equalsIgnoreCase("username")) {
            if (!ValidationUtil.isNullOrBlank(value)) {
                resp = BaseResponseBody.badRequest("Username is not valid!");
                return ResponseEntity.badRequest().body(resp);
            }
            if (userService.isExistedUsername(value)) {
                resp = BaseResponseBody.resourceExist("Username has been used!");
                return ResponseEntity.status(resp.getStatusCode()).body(resp);
            }
        }
        return ResponseEntity.ok().build();
    }


    public ResponseEntity<?> verificationAccount(String code) {
        if (code == null || code.length() != 6) {
            return ResponseEntity.badRequest().build();
        } else {
            User user = userService.findUserByCode(code);
            if (user != null) {
                user.setVerificationCode(null);
                user.setActive(1);
                return ResponseEntity.ok("Account was active!");
            }
            return ResponseEntity.badRequest().body("Code is not valid");
        }
    }

    public ResponseEntity<?> updateMemberAccount(UpdateAccountRequest updateAccountReq) {
//
        String fullName = updateAccountReq.getFullName();
        Integer gender = updateAccountReq.getGender();
        User user = userService.getCurrentUser();
        user.setFullName(fullName);
        user.setGender(gender);
        user.setDob(updateAccountReq.getDob());
        userService.save(user);
        return ResponseEntity.ok("Update user success");
    }

    public ResponseEntity<?> registerMemberAccount(RegisterRequest registerReq) {
        String email = registerReq.getEmail().trim();
        String phone = registerReq.getPhone().trim();
        String password = registerReq.getPassword().trim();

        BaseResponseBody resp;
        if (userService.isExistedEmail(email)) {
            resp = BaseResponseBody.resourceExist("Email has been used!");
            return ResponseEntity.badRequest().body(resp);
        }
        if (userService.isExistedPhone(phone)) {
            resp = BaseResponseBody.resourceExist("Phone has been used!");
            return ResponseEntity.badRequest().body(resp);
        }

        User user = new User();
        user.setEmail(email);
        user.setPhone(phone);
        user.setPassword(password);
        user.setActive(0);
        user.setPassword(passwordEncoder.encode(password));
        user.getRoles().add(userService.getMemberRole());
        //send verification code
        try {
            String code = mailService.sendVerificationCode(email);
            user.setVerificationCode(code);
        } catch (MessagingException | UnsupportedEncodingException e) {
            e.printStackTrace();
            log.error("Failed to create member account {}", e.getMessage());
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
        user = userService.save(user);
        return ResponseEntity.ok("Register account success!");
    }

    public ResponseEntity<?> verifyAccount(String code) {
        User user = userService.findUserByCode(code);
        if (user != null) {
            user.setActive(1);
            user.setVerificationCode(null);
            userService.save(user);
            Map<String, String> tokens = Map.of("access_token", jwtTokenUtil.generateAccessToken(user),
                    "refresh_token", jwtTokenUtil.generateRefreshToken(user)
            );
            return ResponseEntity.ok(tokens);
        }
        return ResponseEntity.badRequest().body(BaseResponseBody.badRequest("Code is not valid!"));
    }


    public ResponseEntity<?> registerShop(ShopRequest shopRequest) {
        User user = userService.getCurrentUser();
        if (userService.getCurrentUser().getShop() == null) {
            Shop shop = new Shop();
            shop.setActive(0);
            shop.setName(shopRequest.getName());
            shop.setDescription(shopRequest.getDescription());
            if (!shopRequest.getEmail().equals(user.getEmail())) {
                return ResponseEntity.badRequest()
                        .body(BaseResponseBody.badRequest("Shop's email is not match your email"));
            }
            shop.setEmail(shopRequest.getEmail());
            if (!shopRequest.getPhone().equals(user.getPhone())) {
                return ResponseEntity.badRequest()
                        .body(BaseResponseBody.badRequest("Shop's phone is not match your phone number"));
            }
            shop.setNumberPhone(shopRequest.getPhone());
            try {
                String code = mailService.sendActiveShopCode(shopRequest.getEmail());
                shop.setVerificationCode(code);
            } catch (MessagingException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            userService.saveShop(shop);

            BaseResponseBody baseResponseBody = new BaseResponseBody();
            baseResponseBody.setMessage("Register shop success");
            baseResponseBody.setStatusCode(200);
            baseResponseBody.setStatusText(HttpStatus.OK.name());
            return ResponseEntity.ok(baseResponseBody);
        } else {
            BaseResponseBody baseResponseBody = new BaseResponseBody();
            baseResponseBody.setMessage("Register shop success");
            baseResponseBody.setStatusCode(422);
            baseResponseBody.setStatusText(HttpStatus.UNPROCESSABLE_ENTITY.name());
            return ResponseEntity.status(422).body(baseResponseBody);
        }
    }

    public ResponseEntity<?> activeShop(String code) {
        boolean b = userService.activateShop(code);
        if (b) {
            return ResponseEntity.ok(BaseResponseBody.successMessage("Active shop success!"));
        }
        return ResponseEntity.badRequest().body(BaseResponseBody.badRequest("Code is not valid!"));
    }

    public ResponseEntity<?> updateShop(UpdateShopRequest updateShopRequest) {
        Shop shop = userService.getCurrentUserShop();
        if (shop == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(BaseResponseBody.notFound("Can not found your shop!"));
        }
        shop.setName(updateShopRequest.getName());
        shop.setStatus(updateShopRequest.getStatus());
        shop.setBanner(updateShopRequest.getBanner());
        shop.setDescription(updateShopRequest.getDescription());
        userService.saveShop(shop);
        return ResponseEntity.ok(BaseResponseBody.successMessage("Update shop success!"));
    }


    public ResponseEntity<?> getCurrentUserDetails() {
        BasicUserInfoResponse currentUserDetails = userService.getCurrentUserDetail();
        return ResponseEntity.ok(BaseResponseBody.success(currentUserDetails, "Get current user detail success", null));
    }

    public ResponseEntity<?> updateCurrentUserDetails(AccountUpdateRequest accountUpdateRequest, MultipartFile multipartFile) {
        return userService.updateCurrentUserDetails(accountUpdateRequest, multipartFile);
    }

    public ResponseEntity<?> getFiles() {
        return userService.getFiles();
    }

    public ResponseEntity<?> getVerificationCode() {
        return userService.getVerificationCode();
    }

    public ResponseEntity<?> resetPassword(ResetPasswordRequest resetPasswordRequest) {
        return userService.resetPassword(resetPasswordRequest);
    }

    public ResponseEntity<?> getAddresses() {
        return userService.getAddresses();
    }

    public ResponseEntity<?> updateAddress(AddressRequest addressRequest) {
        return userService.updateAddress(addressRequest);
    }

    public ResponseEntity<?> deleteAddress(long id) {
        return userService.deleteAddress(id);
    }

    public ResponseEntity<?> getOrders(Map<String, String> params) {
        return userService.getOrders(params);
    }

    public ResponseEntity<?> cancelOrder(Long id) {
        return userService.cancelOrder(id);
    }

    public ResponseEntity<?> getFeedBacks() {
        return userService.getFeedBack();
    }

    public ResponseEntity<?> postFeedback(FeedbackRequest feedbackRequest) {
        return userService.postFeedback(feedbackRequest);
    }

    public ResponseEntity<?> orders(OrderRequest request) {
        return userService.orders(request);
    }


}
