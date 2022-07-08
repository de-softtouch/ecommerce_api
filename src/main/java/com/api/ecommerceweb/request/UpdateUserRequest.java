package com.api.ecommerceweb.request;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class UpdateUserRequest implements Serializable {

    private String fullName;

    private Integer gender;

    private String shopName;

    private Long avtId;
}
