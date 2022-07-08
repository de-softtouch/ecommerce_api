package com.api.ecommerceweb.dto;

import com.api.ecommerceweb.enumm.ERole;
import lombok.*;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RoleDTO implements Serializable {

    private Long id;

    @Enumerated(EnumType.STRING)
    private ERole name;

}
