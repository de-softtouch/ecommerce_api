package com.api.ecommerceweb.reponse;

import lombok.Data;

@Data
public class UserDetail {

    private Long id;

    private String fullName;

    private String phone;

    private String email;

    private Long shopId;

    private String type;

    private String avt;
}
