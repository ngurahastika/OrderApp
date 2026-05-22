package com.example.orderapp.model.order;

import java.math.BigDecimal;

import com.example.orderapp.model.CreatorMod;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "products")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Product extends CreatorMod {

    @Id
    private Long id;

    private String name;

    private BigDecimal price;

    private Integer stock;
    
    
}