package com.github.cashpath.model.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
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
    @Setter(AccessLevel.NONE)
    @OrderColumn(name = "turn_index")
    private List<Player> players = new ArrayList<>();

    @Column(name = "current_turn")
    private int currentTurn;

    @Column(name = "current_day", nullable = false)
    private int currentDay = 1;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private GameStatus status;

    public enum GameStatus {
        ACTIVE, FINISHED
    }

    public void addPlayer(Player player) {
        player.setGame(this);
        players.add(player);
    }
}