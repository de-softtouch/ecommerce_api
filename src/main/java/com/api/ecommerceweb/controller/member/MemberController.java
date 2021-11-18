package com.api.ecommerceweb.controller.member;

import com.api.ecommerceweb.request.AccountUpdateRequest;
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
}
