package com.api.ecommerceweb.controller.member;

import com.api.ecommerceweb.request.*;
import com.api.ecommerceweb.helper.MemberHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/member")
@RequiredArgsConstructor
public class MemberController {

    private final MemberHelper memberHelper;


    @GetMapping
    public ResponseEntity<?> getCurrentUserDetails() {
        return memberHelper.getCurrentUserDetails();
    }

    @PostMapping(consumes = {"multipart/form-data"})
    public ResponseEntity<?> updateCurrentUserDetails(
            @ModelAttribute @Valid AccountUpdateRequest accountUpdateRequest,
            @RequestParam(value = "avt", required = false) MultipartFile multipartFile) {
        return memberHelper.updateCurrentUserDetails(accountUpdateRequest, multipartFile);
    }

    @GetMapping("/addresses")
    public ResponseEntity<?> getAddresses() {
        return memberHelper.getAddresses();
    }

    @PostMapping("/addresses")
    public ResponseEntity<?> updateAddress(@RequestBody @Valid AddressRequest addressRequest) {
        return memberHelper.updateAddress(addressRequest);
    }

    @DeleteMapping("/addresses/{id}")
    public ResponseEntity<?> deleteAddress(@PathVariable long id) {
        return memberHelper.deleteAddress(id);
    }

    @GetMapping("/files")
    public ResponseEntity<?> getFiles() {
        return memberHelper.getFiles();
    }

    @GetMapping("/verification")
    public ResponseEntity<?> getVerificationCode() {
        return memberHelper.getVerificationCode();
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody @Valid ResetPasswordRequest resetPasswordRequest) {
        return memberHelper.resetPassword(resetPasswordRequest);
    }

    @GetMapping("/orders")
    public ResponseEntity<?> getOrders() {
        return memberHelper.getOrders();
    }

    @PostMapping("/orders")
    public ResponseEntity<?> orders(@RequestBody @Valid OrderRequest request) {
        return memberHelper.orders(request);
    }

    @PostMapping("/orders/{id}")
    public ResponseEntity<?> cancelOrder(@PathVariable("id") Long id) {
        return memberHelper.cancelOrder(id);
    }

    @GetMapping("/feedbacks")
    public ResponseEntity<?> getFeedBacks() {
        return memberHelper.getFeedBacks();
    }

    @PostMapping("/feedbacks")
    public ResponseEntity<?> postFeedback(@RequestBody @Valid FeedbackRequest feedbackRequest) {
        return memberHelper.postFeedback(feedbackRequest);
    }

    @PostMapping("/users/shops")
    public ResponseEntity<?> registerShop(@RequestBody ShopRequest shopRequest) {
        return memberHelper.registerShop(shopRequest);
    }

    @GetMapping("/users")
    public ResponseEntity<?> getCurrentUserDetail(){
        return memberHelper.getCurrentUserDetails();
    }

}
