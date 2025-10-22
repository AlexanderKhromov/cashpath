package com.github.cashpath.repository;

import com.github.cashpath.model.entity.Player;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlayerRepository extends JpaRepository<Player, Long> {
    //TODO add methods to avoid N+1 queries
}
