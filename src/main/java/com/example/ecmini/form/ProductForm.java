package com.example.ecmini.form;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ProductForm {

    @NotBlank
    private String name;

    private String author;

    @NotNull
    @Min(0)
    private Integer price;

    @NotBlank
    private String category;

    @NotNull
    @Min(0)
    private Integer stock;

    private String description;

    private String imageUrl;
}