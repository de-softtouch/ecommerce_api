package com.api.ecommerceweb.controller.publicc;

import com.api.ecommerceweb.helper.UserHelper;
import com.api.ecommerceweb.request.AuthRequest;
import com.api.ecommerceweb.request.RegisterRequest;
import com.api.ecommerceweb.helper.AuthHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/public")
@RequiredArgsConstructor
public class AuthController {

    private final AuthHelper authHelper;
    private final UserHelper userHelper;

    @GetMapping("/users/registration/validation")
    public ResponseEntity<?> validation(@RequestParam("input") String input,
                                        @RequestParam("value") String value) {
        return userHelper.validationInputField(input, value);
    }

    /*
    register member account
     */
    @PostMapping("/users/registration")
    public ResponseEntity<?> register(@RequestBody @Valid RegisterRequest registerRequest) {
        return userHelper.registerMemberAccount(registerRequest);
    }

    @GetMapping("/verification")
    public ResponseEntity<?> verifyAccount(@RequestHeader("code") String code) {
        return userHelper.verifyAccount(code);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid AuthRequest authRequest) {
        return authHelper.login(authRequest);
    }

    @GetMapping("/refresh-token")
    public ResponseEntity<?> refreshToken(@RequestHeader(HttpHeaders.AUTHORIZATION) String str) {
        return authHelper.refreshToken(str);
    }
}
