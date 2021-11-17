package com.api.ecommerceweb.mapper;

import com.api.ecommerceweb.dto.RoleDTO;
import com.api.ecommerceweb.dto.UserDTO;
import com.api.ecommerceweb.model.User;

import java.util.Set;
import java.util.stream.Collectors;

public class UserMapper {

    public static UserDTO toUserDto(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setFullName(user.getFullName());
        userDTO.setUsername(user.getUsername());
        userDTO.setGender(user.getGender());
        userDTO.setPhone(user.getPhone());
        userDTO.setDob(user.getDob());
        userDTO.setProfileImg(user.getProfileImg());
        Set<RoleDTO> roleDTOS = user.getRoles().stream().map(RoleMapper::toRoleDto).collect(Collectors.toSet());
        userDTO.setRoles(roleDTOS);
        return userDTO;
    }
}
