package com.api.ecommerceweb.service;

import com.api.ecommerceweb.enumm.AuthenticateProvider;
import com.api.ecommerceweb.enumm.ERole;
import com.api.ecommerceweb.exeption.ValidationException;
import com.api.ecommerceweb.model.Role;
import com.api.ecommerceweb.model.User;
import com.api.ecommerceweb.repository.RoleRepository;
import com.api.ecommerceweb.repository.UserRepository;
import com.api.ecommerceweb.request.AuthRequest;
import com.api.ecommerceweb.request.RegisterRequest;
import com.api.ecommerceweb.security.CustomUserDetails;
import com.api.ecommerceweb.security.JwtTokenUtil;
import com.api.ecommerceweb.utils.MailUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RequiredArgsConstructor
@Component
@Slf4j
public class AuthService {

    private final UserRepository userRepo;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil jwtTokenUtil;
    private final MailUtil mailUtil;
    private final RoleRepository roleRepo;
    private final PasswordEncoder passwordEncoder;


    public ResponseEntity<?> login(AuthRequest authRequest) {
        Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                authRequest.getEmail(), authRequest.getPassword()
        ));
        CustomUserDetails customUserDetails = (CustomUserDetails) authenticate.getPrincipal();
        User user = customUserDetails.getUser();
        Map<String, Object> userInfoResp = toBasicUserInfo(user);
        return ResponseEntity.ok(userInfoResp);
    }

    private Map<String, Object> toBasicUserInfo(User user) {
        Map<String, Object> userInfoResp = new HashMap<>();
        userInfoResp.put("id", user.getId());
        userInfoResp.put("fullName", user.getFullName());
        userInfoResp.put("email", user.getEmail());
        userInfoResp.put("phone", user.getPhone());
        userInfoResp.put("avt", user.getProfileImg());
        userInfoResp.put("access_token", jwtTokenUtil.generateAccessToken(user));
        userInfoResp.put("refresh_token", jwtTokenUtil.generateRefreshToken(user));
        if (user.getShop() != null) {
            userInfoResp.put("shopId", user.getShop().getId());
            userInfoResp.put("type", "seller");
        }
        return userInfoResp;
    }

    public ResponseEntity<?> refreshToken(String str) {
        if (Strings.isEmpty(str) || !str.startsWith("Bearer ")) {
            return ResponseEntity.status(401).build();
        }
        // Get jwt token and validate
        final String token = str.split(" ")[1].trim();
        if (!jwtTokenUtil.validate(token)) {
            return ResponseEntity.status(401).build();
        }
        long id = jwtTokenUtil.getUserId(token);
        User user = userRepo.getById(id);
        Map<String, String> tokens = Map.of(
                "access_token", jwtTokenUtil.generateAccessToken(user),
                "refresh_token", jwtTokenUtil.generateRefreshToken(user)
        );
        return ResponseEntity.ok(tokens);

    }

    public ResponseEntity<?> saveMemberAccount(RegisterRequest registerRequest) {
        Optional<User> optionalUser = userRepo.findByEmail(registerRequest.getEmail());
        if (optionalUser.isPresent())
            return ResponseEntity.badRequest().body("Email has been used!");
        User user = new User();
        user.setActive(0);
        user.setEmail(registerRequest.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setFullName(registerRequest.getFullName());
        user.setGender(registerRequest.getGender());
        user.setAuthProvider(AuthenticateProvider.LOCAL);
        Role role = roleRepo.getByName(ERole.ROLE_MEMBER);
        role.getUsers().add(user);
        user.setRoles(Set.of(role));
        //generate code and send to email
        int randomCode = new Random().nextInt(899999) + 100000;
        user.setVerificationCode(String.valueOf(randomCode));
        try {
            mailUtil.sendVerificationCode(registerRequest.getEmail(), String.valueOf(randomCode));
        } catch (MessagingException e) {
            log.error("MessagingException - {}", e.getMessage());
            return ResponseEntity.internalServerError().build();
        } catch (UnsupportedEncodingException e) {
            log.error("UnsupportedEncodingException - {}", e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
        userRepo.save(user);
        return ResponseEntity.ok(registerRequest);

    }

    public ResponseEntity<?> verifyAccount(String code) {
        Optional<User> optionalUser = userRepo.findByVerificationCode(code);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setActive(1);
            user.setVerificationCode(null);
            userRepo.save(user);
            Map<String, Object> resp = toBasicUserInfo(user);
            return ResponseEntity.ok(resp);
        }
        return ResponseEntity.badRequest().body("Code is not valid");
    }

    public boolean validationRequestInput(String input, String value) throws ValidationException {
        input = input.trim();
        value = value.trim();
        if (input.isBlank() || value.isBlank()) {
            return false;
        }
        if (input.equalsIgnoreCase("email")) {
            if (!validEmail(value)) {
                throw new ValidationException("Email is not valid");
            }
            if (userRepo.existsByEmail(value)) {
                throw new ValidationException("Email has been used");
            }
            return true;
        } else if (input.equals("phone")) {
            if (!validateMobileNumber(value)) {
                throw new ValidationException("Phone is not valid");
            }
            if (userRepo.existsByPhone(value)) {
                throw new ValidationException("Phone number has been used");
            }
            return true;
        }
        return false;
    }

    public boolean validEmail(String email) {
        Pattern pattern = Pattern.compile("^.+@.+\\..+$");
        Matcher matcher = pattern.matcher(email.trim());
        if (matcher.matches()) {
            return true;
        }
        return false;
    }

    public boolean validateMobileNumber(String mobileNumber) {
//        {"2055550125","202 555 0125", "(202) 555-0125", "+111 (202) 555-0125",
//                "636 856 789", "+111 636 856 789", "636 85 67 89", "+111 636 85 67 89"}
        String patterns
                = "^(\\+\\d{1,3}( )?)?((\\(\\d{3}\\))|\\d{3})[- .]?\\d{3}[- .]?\\d{4}$"
                + "|^(\\+\\d{1,3}( )?)?(\\d{3}[ ]?){2}\\d{3}$"
                + "|^(\\+\\d{1,3}( )?)?(\\d{3}[ ]?)(\\d{2}[ ]?){2}\\d{2}$";
        Pattern regexPattern = Pattern.compile(patterns);
        Matcher regMatcher = regexPattern.matcher(mobileNumber);
        if (regMatcher.matches()) {
            return true;
        }
        return false;
    }
}
