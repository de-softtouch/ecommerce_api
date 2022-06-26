package com.api.ecommerceweb.reponse;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.LinkedList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VariantResponse {

    private Long id;

    private String name;

    private List<String> images = new LinkedList<>();

    private List<String> options = new LinkedList<>();
}
