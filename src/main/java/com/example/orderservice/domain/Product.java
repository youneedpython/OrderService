package com.example.orderservice.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private long price;

    @Column(nullable = false, length = 1000)
    private String description;

    protected Product() {}

    public Product(String name, long price, String description) {
        if (price < 0) {
            throw new IllegalArgumentException("상품 가격은 0 이상이어야 합니다.");
        }
        this.name = name;
        this.price = price;
        this.description = description;
    }

    public void updateInfo(String name, long price, String description) {
        if (price < 0) {
            throw new IllegalArgumentException("상품 가격은 0 이상이어야 합니다.");
        }
        this.name = name;
        this.price = price;
        this.description = description;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public long getPrice() { return price; }
    public String getDescription() { return description; }
}
