package com.api.ecommerceweb.reponse;

import lombok.*;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse implements Serializable {

    private String message;

    private List<String> details;

}
