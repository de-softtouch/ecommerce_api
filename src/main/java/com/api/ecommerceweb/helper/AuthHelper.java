package com.api.ecommerceweb.helper;

import com.api.ecommerceweb.exeption.ValidationException;
import com.api.ecommerceweb.request.AuthRequest;
import com.api.ecommerceweb.request.RegisterRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthHelper {

    private final com.api.ecommerceweb.service.AuthService authService;

    public ResponseEntity<?> login(AuthRequest authRequest) {
        return authService.login(authRequest);
    }

    public ResponseEntity<?> refreshToken(String str) {
        return authService.refreshToken(str);
    }

    public ResponseEntity<?> register(RegisterRequest registerRequest) {
        return authService.saveMemberAccount(registerRequest);
    }

    public ResponseEntity<?> verifyAccount(String code) {
        return authService.verifyAccount(code);
    }

    public ResponseEntity<?> validation(String input, String value) {

        try {
            boolean valid = authService.validationRequestInput(input, value);
            if (valid) {
                return ResponseEntity.ok("Valid");
            }
            return ResponseEntity.status(422).build();
        } catch (ValidationException e) {
            return ResponseEntity.status(422).body(Map.of("message", e.getMessage()));
        }
    }
}
