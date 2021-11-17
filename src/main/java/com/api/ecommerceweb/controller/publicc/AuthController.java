package com.api.ecommerceweb.controller.publicc;

import com.api.ecommerceweb.request.AuthRequest;
import com.api.ecommerceweb.request.RegisterRequest;
import com.api.ecommerceweb.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/public")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @GetMapping("/test")
    public ResponseEntity<?> test() {
        return ResponseEntity.ok("Test.....");

    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody @Valid RegisterRequest registerRequest) {
        return authService.register(registerRequest);
    }

    @GetMapping("/verification")
    public ResponseEntity<?> verifyAccount(@RequestHeader("code") String code) {
        return authService.verifyAccount(code);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid AuthRequest authRequest) {
        return authService.login(authRequest);
    }

    @GetMapping("/refresh-token")
    public ResponseEntity<?> refreshToken(@RequestHeader(HttpHeaders.AUTHORIZATION) String str) {
        return authService.refreshToken(str);
    }
}
