package com.api.ecommerceweb.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class ResetPasswordRequest {

    @NotBlank
    @Size(min = 6)
    private String newPassword;

    @NotBlank
    @Size(min = 6)
    private String oldPassword;

    @NotBlank
    @Size(min = 6, max = 6)
    private String verificationCode;
}
