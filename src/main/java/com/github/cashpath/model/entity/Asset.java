package com.github.cashpath.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "assets")
public class Asset {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private double price;
    @Column(name = "daily_cash_flow")
    private double dailyCashFlow;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AssetType type;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private Player owner;

    @SuppressWarnings("unused")
    public enum AssetType {
        REAL_ESTATE, STOCK, BUSINESS
    }
}