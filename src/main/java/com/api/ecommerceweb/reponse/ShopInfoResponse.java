package com.api.ecommerceweb.reponse;

import lombok.*;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ShopInfoResponse implements Serializable {

    private Long id;

    private String name;


}
