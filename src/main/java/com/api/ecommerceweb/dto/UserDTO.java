package com.api.ecommerceweb.dto;

import com.api.ecommerceweb.enumm.AuthenticateProvider;
import lombok.Data;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;


@Data
public class UserDTO {

    private Long id;

    private String email;

    private String phone;

    private String password;

    private String fullName;

    private Integer gender;

    private Integer verificationCode;

    private Integer active;

    private AuthenticateProvider authProvider;

    private Set<RoleDTO> roles = new HashSet<>();

    private Date createDate;

    private Date modifyDate;

    private Set<AddressDTO> addresses = new HashSet<>();


}
