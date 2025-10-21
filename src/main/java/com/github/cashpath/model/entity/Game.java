package com.github.cashpath.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "games")
public class Game {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "game", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Player> players;

    @Column(name = "current_turn")
    private int currentTurn;

    @Column(name = "current_day", nullable = false)
    private LocalDate currentDay = LocalDate.now();

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private GameStatus status;

    @SuppressWarnings("unused")
    public enum GameStatus {
        ACTIVE, FINISHED
    }
}