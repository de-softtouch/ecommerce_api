package com.api.ecommerceweb.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
public class AddressRequest {

    private Long id;

    @NotBlank
    private String fullName;

    @NotBlank
    @Pattern(regexp = "\\d{10}|(?:\\d{3}-){2}\\d{4}|\\(\\d{3}\\)\\d{3}-?\\d{4}")
    private String phone;

    @NotBlank
    @Pattern(regexp = "[0-9]+")
    private String postCode;

    @NotBlank
    @Size(min = 20)
    private String addressDetails;

    //type of address is home(0) or work(1) place
    private Integer type = 0;

    private Integer isDefault = 0;

}
