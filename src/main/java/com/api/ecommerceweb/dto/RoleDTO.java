package com.api.ecommerceweb.dto;

import com.api.ecommerceweb.enumm.ERole;
import com.api.ecommerceweb.model.User;
import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Data
public class RoleDTO {

    private Long id;

    private ERole name;

}
