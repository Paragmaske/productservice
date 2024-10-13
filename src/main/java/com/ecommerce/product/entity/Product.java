package com.ecommerce.product.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "product_id_seq")
    @SequenceGenerator(name="product_id_seq",initialValue = 1,allocationSize = 1)
    private long productId;

    @Column(name="product_name")
    private String productName;

    @Column(name="price")
    private Double price;

    @Column(name="quantity")
    private Long quantity;
}
