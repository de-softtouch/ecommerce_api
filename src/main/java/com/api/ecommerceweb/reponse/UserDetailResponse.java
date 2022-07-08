package com.api.ecommerceweb.reponse;

import com.api.ecommerceweb.dto.RoleDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDetailResponse implements Serializable {


    private Long id;

    private String username;

    private String fullName;

    private String avt;

    private String phone;

    private String email;

    private Integer gender;

    private ShopInfoResponse shop;

    private Set<RoleDTO> roles = new HashSet<>();
}
