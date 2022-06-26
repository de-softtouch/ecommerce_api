package com.api.ecommerceweb.request;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class AuthRequest {


    private String fullName;

    private String phone;

    private Integer gender;

    @NotBlank(message = "Email may not be blank")
    @Email(message = "Email is not valid")
    private String email;

    @NotBlank(message = "Password may not be blank")
    @Size(min = 6, message = "Password at least 6 characters")
    private String password;
}
