package com.api.ecommerceweb.helper;

import com.api.ecommerceweb.request.ResetPasswordRequest;
import com.api.ecommerceweb.model.Shop;
import com.api.ecommerceweb.model.User;
import com.api.ecommerceweb.request.*;
import com.api.ecommerceweb.service.AddressService;
import com.api.ecommerceweb.service.MemberService;
import com.api.ecommerceweb.service.ShopService;
import com.api.ecommerceweb.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


@Service
@RequiredArgsConstructor
public class MemberHelper {

    private final MemberService memberService;
    private final UserService userService;
    private final ShopService shopService;
    private final AddressService addressService;

    public ResponseEntity<?> getCurrentUserDetails() {
        return memberService.getCurrentUserDetails();
    }

    public ResponseEntity<?> updateCurrentUserDetails(AccountUpdateRequest accountUpdateRequest, MultipartFile multipartFile) {
        return memberService.updateCurrentUserDetails(accountUpdateRequest, multipartFile);
    }

    public ResponseEntity<?> getFiles() {
        return memberService.getFiles();
    }

    public ResponseEntity<?> getVerificationCode() {
        return memberService.getVerificationCode();
    }

    public ResponseEntity<?> resetPassword(ResetPasswordRequest resetPasswordRequest) {
        return memberService.resetPassword(resetPasswordRequest);
    }

    public ResponseEntity<?> getAddresses() {
        return memberService.getAddresses();
    }

    public ResponseEntity<?> updateAddress(AddressRequest addressRequest) {
        return memberService.updateAddress(addressRequest);
    }

    public ResponseEntity<?> deleteAddress(long id) {
        return memberService.deleteAddress(id);
    }

    public ResponseEntity<?> getOrders() {
        return memberService.getOrders();
    }

    public ResponseEntity<?> cancelOrder(Long id) {
        return memberService.cancelOrder(id);
    }

    public ResponseEntity<?> getFeedBacks() {
        return memberService.getFeedBack();
    }

    public ResponseEntity<?> postFeedback(FeedbackRequest feedbackRequest) {
        return memberService.postFeedback(feedbackRequest);
    }

    public ResponseEntity<?> orders(OrderRequest request) {
        return memberService.orders(request);
    }

    public ResponseEntity<?> registerShop(ShopRequest shopRequest) {
        User user = userService.getCurrentUser();
        Shop currentUserShop = shopService.getShop(user);
        if (currentUserShop == null) {
            Shop shop = userService.saveShop(shopRequest);
            return ResponseEntity.ok("Save shop suucess");
        } else {
            return ResponseEntity.badRequest().body("User already has a shop");
        }

    }


}
