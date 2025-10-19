package com.github.cashpath.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "liabilities")
public class Liability {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String name;
    @Column(name = "monthly_payment")
    private double monthlyPayment;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private Player owner;
}
