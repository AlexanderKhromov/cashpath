package com.github.cashpath.repository;

import com.github.cashpath.model.entity.Player;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlayerRepository extends JpaRepository<Player, Long> {
    //TODO add methods to avoid N+1 queries
    @EntityGraph(attributePaths = "liabilities")
    List<Player> findAllByGameId(Long gameId);
}
