package com.api.ecommerceweb.reponse;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class SizeResponse {

    private Long id;

    private String label;

    private String size;

}
