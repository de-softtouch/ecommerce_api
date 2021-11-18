package com.api.ecommerceweb.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Date;

@Data
public class AccountUpdateRequest {


    @NotBlank(message = "Full name may not be blank!")
    @Size(min = 3, message = "Full name at least 3 characters!")
    private String fullName;

    private Integer gender = 2;

    private Date dob;
}
