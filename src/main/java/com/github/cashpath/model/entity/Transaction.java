package com.github.cashpath.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "transactions")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //TODO Consider about using @ManyToOne relationship with Player for better usage of JOIN FETCH
    @Column(name="from_player_id")
    private Long fromPlayerId;

    //TODO Consider about using @ManyToOne relationship with Player for better usage of JOIN FETCH
    @Column(name = "to_player_id")
    private Long toPlayerId;
    @Column
    private double amount;

    @Enumerated(EnumType.STRING)
    @Column
    private TransactionType type;
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @SuppressWarnings("unused")
    public enum TransactionType {
        INCOME, EXPENSE, INVESTMENT
    }

    @PrePersist
    private void prePersist() {
        createdAt = LocalDateTime.now();
    }

}