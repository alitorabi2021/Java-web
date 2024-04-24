package com.example.myproject.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Calendar;
import java.util.Date;

@Data
@EqualsAndHashCode
public class ProductDto {
    private Long id;
    private String brand;
    private String name;
    private String category;
    private Long price;
    private String status;
    private Date createAt;


    public ProductDto(Long id,String brand, String name, String category,
                      Long price, String status) {
        this.id=id;
        this.brand = brand;
        this.name = name;
        this.category = category;
        this.price = price;
        this.status = status;
    }
    public ProductDto() {
    }
}
