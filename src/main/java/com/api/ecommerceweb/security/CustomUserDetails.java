package com.api.ecommerceweb.security;

import com.api.ecommerceweb.dto.RoleDTO;
import com.api.ecommerceweb.dto.UserDTO;
import com.api.ecommerceweb.enumm.AuthenticateProvider;
import com.api.ecommerceweb.mapper.RoleMapper;
import com.api.ecommerceweb.mapper.UserMapper;
import com.api.ecommerceweb.model.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
public class CustomUserDetails extends User implements UserDetails  {


    public CustomUserDetails(User user) {
        super(user.getId(),
                user.getUsername(),
                user.getFullName(),
                user.getPhone(),
                user.getEmail(),
                user.getPassword(),
                user.getVerificationCode(),
                user.getProfileImg(),
                user.getActive(),
                user.getStatus(),
                user.getCreateDate(),
                user.getUpdateDate(),
                user.getRoles());
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<Role> roles = super.getRoles();
        List<RoleDTO> list = roles.stream().map(RoleMapper::toRoleDto).collect(Collectors.toList());
        return list.stream()
                .map(roleDTO -> new SimpleGrantedAuthority(roleDTO.getName().name()))
                .collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return super.getPassword();
    }

    @Override
    public String getUsername() {
        return super.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return super.getActive().equals(1);
    }
}
