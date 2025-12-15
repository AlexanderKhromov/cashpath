package com.github.cashpath.repository;

import com.github.cashpath.model.entity.Player;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlayerRepository extends JpaRepository<Player, Long> {

    @Override
    @NonNull
    @EntityGraph(attributePaths = {"assets", "liabilities"})
    List<Player> findAll();

}
