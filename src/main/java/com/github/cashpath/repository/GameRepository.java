package com.github.cashpath.repository;

import com.github.cashpath.model.entity.Game;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GameRepository extends JpaRepository<Game, Long> {

    @EntityGraph(attributePaths = {
            "players",
            "players.assets",
            "players.assets.owner"
    })
    Optional<Game> findWithPlayersAndAssetsById(Long id);
}
