package com.api.ecommerceweb.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
public class UpdateAccountRequest {


    @NotBlank
    private String fullName;

    @NotBlank
    private String username;

    private Integer gender = 2;

    @NotNull
    private Date dob;
}
