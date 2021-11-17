package com.api.ecommerceweb.helper;

import com.api.ecommerceweb.dto.UserDTO;
import com.api.ecommerceweb.mapper.UserMapper;
import com.api.ecommerceweb.repository.UserRepository;
import com.api.ecommerceweb.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component("MemberHelper")
@RequiredArgsConstructor
public class MemberHelper {

    private final UserRepository userRepo;

    public ResponseEntity<?> getCurrentUserDetails() {
        CustomUserDetails principal = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserDTO dto = UserMapper.toUserDto(principal.getUser());
        return ResponseEntity.ok(dto);
    }
}
