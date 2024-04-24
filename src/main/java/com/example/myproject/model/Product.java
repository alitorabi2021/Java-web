package com.example.myproject.model;


import javax.persistence.*;

import lombok.*;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;

@Entity
@Setter
@Getter
@Table(name = "t_product")
@AllArgsConstructor
public class  Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String brand;
    private String name;
    private String category;
    private Long price;
    private String status;
    @Temporal(TemporalType.DATE)
    private Date createAt;

    public Product(String brand, String name, String category, Long price, String status, Date createAt) {
        this.brand = brand;
        this.name = name;
        this.category = category;
        this.price = price;
        this.status = status;
        this.createAt = createAt;
    }

    public Product() {
    }
}

