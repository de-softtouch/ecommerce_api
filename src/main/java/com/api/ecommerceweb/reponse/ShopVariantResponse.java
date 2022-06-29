package com.api.ecommerceweb.reponse;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ShopVariantResponse implements Serializable {

    private Long id;

    private String name;

    private List<String> images = new LinkedList<>();

    private List<VariantOptionResponse> options = new LinkedList<>();
}
