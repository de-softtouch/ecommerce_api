package com.api.ecommerceweb.service;

import com.api.ecommerceweb.helper.AuthHelper;
import com.api.ecommerceweb.request.AuthRequest;
import com.api.ecommerceweb.request.RegisterRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthHelper authHelper;

    public ResponseEntity<?> login(AuthRequest authRequest) {
        return authHelper.login(authRequest);
    }

    public ResponseEntity<?> refreshToken(String str) {
        return authHelper.refreshToken(str);
    }

    public ResponseEntity<?> register(RegisterRequest registerRequest) {
        return authHelper.saveMemberAccount(registerRequest);
    }

    public ResponseEntity<?> verifyAccount(String code) {
        return authHelper.verifyAccount(code);
    }
}
