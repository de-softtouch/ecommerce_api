package com.api.ecommerceweb.request;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class FeedbackRequest {

    private Long id;

    @NotBlank
    private String comment;

    @NotNull(message = "numericField: positive number value is required")
    @Min(value = 0)
    @Max(value = 5)
    private Integer rating;

    @NotNull(message = "numericField: positive number value is required")
    private Long productId;

}
