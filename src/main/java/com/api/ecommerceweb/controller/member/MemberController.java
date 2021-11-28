package com.api.ecommerceweb.controller.member;

import com.api.ecommerceweb.request.AccountUpdateRequest;
import com.api.ecommerceweb.request.AddressRequest;
import com.api.ecommerceweb.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/member")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;


    @GetMapping
    public ResponseEntity<?> getCurrentUserDetails() {
        return memberService.getCurrentUserDetails();
    }

    @PostMapping(consumes = {"multipart/form-data"})
    public ResponseEntity<?> updateCurrentUserDetails(
            @ModelAttribute @Valid AccountUpdateRequest accountUpdateRequest,
            @RequestParam(value = "avt", required = false) MultipartFile multipartFile) {
        return memberService.updateCurrentUserDetails(accountUpdateRequest, multipartFile);
    }

    @GetMapping("/addresses")
    public ResponseEntity<?> getAddresses() {
        return memberService.getAddresses();
    }

    @PostMapping("/addresses")
    public ResponseEntity<?> updateAddress(@RequestBody @Valid AddressRequest addressRequest) {
        return memberService.updateAddress(addressRequest);
    }

    @DeleteMapping("/addresses/{id}")
    public ResponseEntity<?> deleteAddress(@PathVariable long id) {
        return memberService.deleteAddress(id);
    }

    @GetMapping("/files")
    public ResponseEntity<?> getFiles() {
        return memberService.getFiles();
    }

    @GetMapping("/verification")
    public ResponseEntity<?> getVerificationCode() {
        return memberService.getVerificationCode();
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody @Valid ResetPasswordRequest resetPasswordRequest) {
        return memberService.resetPassword(resetPasswordRequest);
    }

    @GetMapping("/orders")
    public ResponseEntity<?> getOrders() {
        return memberService.getOrders();
    }

    @PostMapping("/orders/{id}")
    public ResponseEntity<?> cancelOrder(@PathVariable("id") Long id) {
        return memberService.cancelOrder(id);

    }
}
