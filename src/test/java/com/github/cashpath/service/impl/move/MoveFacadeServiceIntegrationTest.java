package com.github.cashpath.service.impl.move;
import com.github.cashpath.model.dto.BuyRequestDTO;
import com.github.cashpath.model.dto.MoveResponseDTO;
import com.github.cashpath.model.entity.Game;
import com.github.cashpath.model.entity.OpportunityCard;
import com.github.cashpath.model.entity.Player;
import com.github.cashpath.repository.GameRepository;
import com.github.cashpath.repository.PlayerRepository;
import com.github.cashpath.service.game.GameLifecycleService;
import com.github.cashpath.service.move.MoveFacadeService;
import com.github.cashpath.service.opportunity.OpportunityService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class MoveFacadeServiceIntegrationTest {

    @Autowired
    private MoveFacadeService moveService;

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private OpportunityService opportunityService;

    @Autowired
    private GameLifecycleService gameLifecycleService;

    private Game game;
    private Player player;
    private OpportunityCard card;

    @BeforeEach
    void setUp() {
        player = new Player();
        player.setName("Alex");
        player.setCash(1000.0);

        game = new Game();
        game.setCurrentTurn(0);
        game.setStatus(Game.GameStatus.ACTIVE);
        game.addPlayer(player);
        game = gameRepository.save(game);

        opportunityService.initCardsForGame(game);

        card = opportunityService.getRandomCard(game.getId());
    }

    @Test
    @DisplayName("buy card integration test")
    void testBuyIntegration() {
        BuyRequestDTO request = new BuyRequestDTO(card.getId());

        MoveResponseDTO response = moveService.buy(game.getId(), request);

        assertNotNull(response);
        assertEquals(game.getId(), response.game().id());
        assertTrue(playerRepository.findById(player.getId()).isPresent());
    }

    @Test
    @DisplayName("endTurn integration test")
    void testEndTurnIntegration() {
        MoveResponseDTO response = moveService.endTurn(game.getId());

        assertNotNull(response);
        assertEquals(game.getId(), response.game().id());
    }
}
