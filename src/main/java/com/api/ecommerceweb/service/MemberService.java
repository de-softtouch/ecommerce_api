package com.api.ecommerceweb.service;

import com.api.ecommerceweb.controller.member.ResetPasswordRequest;
import com.api.ecommerceweb.helper.MemberHelper;
import com.api.ecommerceweb.request.AccountUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberHelper memberHelper;

    public ResponseEntity<?> getCurrentUserDetails() {
        return memberHelper.getCurrentUserDetails();
    }

    public ResponseEntity<?> updateCurrentUserDetails(AccountUpdateRequest accountUpdateRequest, MultipartFile multipartFile) {
        return memberHelper.updateCurrentUserDetails(accountUpdateRequest, multipartFile);
    }

    public ResponseEntity<?> getFiles() {
        return memberHelper.getFiles();
    }

    public ResponseEntity<?> getVerificationCode() {
        return memberHelper.getVerificationCode();
    }

    public ResponseEntity<?> resetPassword(ResetPasswordRequest resetPasswordRequest) {
        return memberHelper.resetPassword(resetPasswordRequest);
    }
}
