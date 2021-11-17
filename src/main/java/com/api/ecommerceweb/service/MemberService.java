package com.api.ecommerceweb.service;

import com.api.ecommerceweb.helper.MemberHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberHelper memberHelper;

    public ResponseEntity<?> getCurrentUserDetails() {
        return memberHelper.getCurrentUserDetails();
    }
}
