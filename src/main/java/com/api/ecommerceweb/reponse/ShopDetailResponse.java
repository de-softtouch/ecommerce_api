package com.api.ecommerceweb.reponse;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ShopDetailResponse implements Serializable {

    private Long id;

    private String name;

    private String description;

    private String avt;

    private String banner;

    private Date createDate;

    private Integer productCount;

    private Integer likeCount;
}
