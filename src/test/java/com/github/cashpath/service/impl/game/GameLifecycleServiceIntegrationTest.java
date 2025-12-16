package com.github.cashpath.service.impl.game;

import com.github.cashpath.model.entity.Game;
import com.github.cashpath.model.entity.Player;
import com.github.cashpath.repository.PlayerRepository;
import com.github.cashpath.service.game.GameLifecycleService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class GameLifecycleServiceIntegrationTest {

    @Autowired
    private GameLifecycleService service;
    @Autowired
    private PlayerRepository playerRepository;

    @Test
    @DisplayName("Integration test: createGame creates correct bean and sets first player")
    void createGame_integration() {
        Player a = new Player();
        a.setName("Alex");

        Player b = new Player();
        b.setName("Nadya");
        Game game = service.createGame(List.of(a, b));

        assertNotNull(game.getId());
        assertEquals(a, game.getPlayers().get(game.getCurrentTurn()));
        assertEquals(Game.GameStatus.ACTIVE, game.getStatus());
    }
}
