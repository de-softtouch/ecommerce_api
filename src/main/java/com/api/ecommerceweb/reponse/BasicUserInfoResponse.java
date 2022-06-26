package com.api.ecommerceweb.reponse;

import com.api.ecommerceweb.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BasicUserInfoResponse {

    private Long id;
    private String username;

    private String fullName;

    private String phone;

    private String email;

    private Integer gender;





}
