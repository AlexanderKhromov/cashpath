package com.github.cashpath.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "opportunity_cards")
public class OpportunityCard {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OpportunityType type;

    @Column(nullable = false)
    private double amount;

    @ManyToOne
    @JoinColumn(name = "asset_id")
    private Asset asset;

    //DOODAD means buying some product that does not bring you passive income
    public enum OpportunityType {
        SMALL_DEAL, BIG_DEAL, DOODAD
    }
}
