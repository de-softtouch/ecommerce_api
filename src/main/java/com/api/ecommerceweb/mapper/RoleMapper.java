package com.api.ecommerceweb.mapper;

import com.api.ecommerceweb.dto.RoleDTO;
import com.api.ecommerceweb.model.Role;

public class RoleMapper {

    public static RoleDTO toRoleDto(Role role) {
        RoleDTO roleDTO = new RoleDTO();
        roleDTO.setName(role.getName());
        return roleDTO;
    }
}
