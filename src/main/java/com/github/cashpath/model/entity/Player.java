package com.github.cashpath.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "players")
public class Player {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;
    @Column
    private double cash;
    @Column
    private double salary;
    @Column(name = "monthly_expenses")
    private double monthlyExpenses;
    @Column(name = "on_fast_track")
    private boolean onFastTrack;

    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Asset> assets = new ArrayList<>();

    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Liability> liabilities = new ArrayList<>();

    @ManyToOne
    @JoinColumn(nullable = false, name = "game_id")
    private Game game;
}
