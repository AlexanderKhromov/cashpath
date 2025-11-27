package com.github.cashpath.service.impl.game;

import static org.junit.jupiter.api.Assertions.*;

import com.github.cashpath.exception.PlayersNotFoundInException;
import com.github.cashpath.model.entity.Asset;
import com.github.cashpath.model.entity.Game;
import com.github.cashpath.model.entity.Liability;
import com.github.cashpath.model.entity.Player;
import com.github.cashpath.repository.GameRepository;
import com.github.cashpath.util.PlayerInitializer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.mockito.Mockito.*;

class GameLifecycleServiceImplTest {
    private final GameRepository gameRepository = mock(GameRepository.class);
    private final PlayerInitializer initializer = mock(PlayerInitializer.class);

    private final GameLifecycleServiceImpl service =
            new GameLifecycleServiceImpl(gameRepository, initializer);

    // -------- createGame --------

    @Test
    @DisplayName("createGame: should initialize players and create active game")
    void createGame_initializesPlayersAndSetsFirstTurn() {
        Player a = createPlayer(1L, "Alex");
        Player b = createPlayer(2L, "Nadya");

        when(initializer.generateRandomSalary()).thenReturn(10000.0);
        when(initializer.generateRandomCash(10000.0)).thenReturn(5000.0);
        Liability l = new Liability();
        l.setName("Loan");
        l.setDailyPayment(300);
        when(initializer.generateLiabilities(10000.0)).thenReturn(Set.of(l));

        when(gameRepository.save(Mockito.any(Game.class))).thenAnswer(i -> i.getArgument(0));

        Game game = service.createGame(List.of(a, b));

        assertNotNull(game);
        assertEquals(Game.GameStatus.ACTIVE, game.getStatus());
        assertEquals(2, game.getPlayers().size());
        assertEquals(a, game.getPlayers().getFirst());
    }

    // ---------- switchTurn ----------

    @Test
    @DisplayName("switchTurn: should rotate current player")
    void switchTurn_rotatesCorrectly() {
        Player a = createPlayer(1L, "Alex");
        Player b = createPlayer(2L, "Nadya");
        Player c = createPlayer(3L, "John");

        Game game = new Game();
        game.addPlayer(a);
        game.addPlayer(b);
        game.addPlayer(c);
        game.setCurrentTurn(0);
        game.setCurrentDay(1);

        when(gameRepository.save(any(Game.class))).thenAnswer(i -> i.getArgument(0));

        service.switchTurn(game);
        assertEquals(1, game.getCurrentTurn());

        service.switchTurn(game);
        assertEquals(2, game.getCurrentTurn());

        service.switchTurn(game);
        assertEquals(0, game.getCurrentTurn());
    }

    @Test
    @DisplayName("switchTurn: should throw exception if game has no players")
    void switchTurn_noPlayers_throws() {
        Game game = new Game();
        assertThrows(PlayersNotFoundInException.class, () -> service.switchTurn(game));
    }

    // ---------- checkWinCondition ----------

    @Test
    @DisplayName("checkWinCondition: should return true when passive income >= expenses")
    void checkWinCondition_true() {
        Player a = createPlayer(1L, "Alex");

        Asset asset = new Asset();
        asset.setDailyCashFlow(5000);
        asset.setOwner(a);

        a.getAssets().add(asset);
        a.setDailyExpenses(3000);

        boolean result = service.checkWinCondition(a);

        assertTrue(result);
    }

    @Test
    @DisplayName("checkWinCondition: should return false when passive income < expenses")
    void checkWinCondition_false() {
        Player a = createPlayer(1L, "Alex");
        Asset asset = new Asset();
        asset.setDailyCashFlow(1000);
        asset.setOwner(a);

        a.getAssets().add(asset);
        a.setDailyExpenses(3000);

        boolean result = service.checkWinCondition(a);

        assertFalse(result);
    }

    // ---------- helpers ----------
    private Player createPlayer(long id, String name) {
        Player p = new Player();
        p.setId(id);
        p.setName(name);
        return p;
    }
}