package com.api.ecommerceweb.reponse;

import com.api.ecommerceweb.model.User;
import lombok.*;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class BasicUserInfoResponse implements Serializable {

    private Long id;

    private String username;

    private String fullName;

    private String phone;

    private String email;

    private Integer gender;


}
