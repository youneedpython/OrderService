package com.example.orderservice.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "stocks")
public class Stock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false, unique = true)
    private Product product;

    @Column(nullable = false)
    private int quantity;

    @Version
    private long version;

    protected Stock() {}

    public Stock(Product product, int quantity) {
        if (quantity < 0) {
            throw new IllegalArgumentException("재고 수량은 0 이상이어야 합니다.");
        }
        this.product = product;
        this.quantity = quantity;
    }

    public void decrease(int amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("차감 수량은 1 이상이어야 합니다.");
        }
        if (this.quantity < amount) {
            throw new IllegalStateException("재고가 부족합니다. 현재 재고: " + this.quantity);
        }
        this.quantity -= amount;
    }

    public void increase(int amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("증가 수량은 1 이상이어야 합니다.");
        }
        this.quantity += amount;
    }

    public Long getId() { return id; }
    public Product getProduct() { return product; }
    public int getQuantity() { return quantity; }
    public long getVersion() { return version; }
}
