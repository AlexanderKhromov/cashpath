package com.github.cashpath.model.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "opportunity_cards")
public class OpportunityCard {
    @Id
    //with IDENTITY, Hibernate won't do batch queries — there will be INSERT one by one just like now.
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    //TODO the following 2 lines is for batch insert/update inte the table
    //@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "opportunity_seq")
    //@SequenceGenerator(name = "opportunity_seq", sequenceName = "opportunity_seq", allocationSize = 50)
    private Long id;

    @Column(nullable = false)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OpportunityType type;

    @Column(nullable = false)
    private double amount;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "asset_id")
    private Asset asset;

    /**
     * true — may be shown to players, false — is already bought by one
     * Initially card is available to players
     */
    @Setter(AccessLevel.NONE)
    @Column(name = "is_available", nullable = false)
    private boolean isAvailable = true;

    /**
     * To mark that card is no more available (e.g. business is bought by someone)
     */
    public void markAsUnavailable() {
        this.isAvailable = false;
    }

    //DOODAD means buying some product that does not bring you passive income
    public enum OpportunityType {
        SMALL_DEAL, BIG_DEAL, DOODAD
    }

}
