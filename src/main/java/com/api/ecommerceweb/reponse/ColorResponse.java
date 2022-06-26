package com.api.ecommerceweb.reponse;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ColorResponse {

    private Long id;

    private String code;

    private String label;
}
