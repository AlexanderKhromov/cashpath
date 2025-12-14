package com.github.cashpath.service.impl.game;

import com.github.cashpath.exception.GameNotFoundException;
import com.github.cashpath.exception.PlayersNotFoundInException;
import com.github.cashpath.model.entity.*;
import com.github.cashpath.repository.GameRepository;
import com.github.cashpath.repository.OpportunityCardRepository;
import com.github.cashpath.service.finance.PlayerFinanceService;
import com.github.cashpath.service.game.GameLifecycleService;
import com.github.cashpath.service.opportunity.OpportunityService;
import com.github.cashpath.util.PlayerInitializer;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
@Log4j2
public class GameLifecycleServiceImpl implements GameLifecycleService {
    private final GameRepository gameRepository;
    private final PlayerInitializer initializer;
    private final PlayerFinanceService financeService;
    private final OpportunityService opportunityService;

    @Override
    public Game getGame(Long gameId) {
        return gameRepository.findById(gameId).orElseThrow(() -> new GameNotFoundException(gameId));
    }

    @Transactional
    @Override
    public Game createGame(List<Player> players) {
        Game game = new Game();
        game.setStatus(Game.GameStatus.ACTIVE);

        for (Player p : players) {
            initPlayer(p);
            game.addPlayer(p);
        }
        Game savedGame = gameRepository.save(game);
        opportunityService.initCardsForGame(savedGame);
        return savedGame;
    }

    @Transactional
    @Override
    public void switchTurn(Game game) {
        List<Player> players = game.getPlayers();
        if (players.isEmpty()) throw new PlayersNotFoundInException(game.getId());
        Player currentPlayer = financeService.getCurrentPlayer(game);

        int nextTurn = (game.getCurrentTurn() + 1) % players.size();
        game.setCurrentTurn(nextTurn);

        updateCash(currentPlayer);
        if (nextTurn == 0) {
            game.setCurrentDay(game.getCurrentDay() + 1);
        }

        gameRepository.save(game);
    }

    @Override
    public void buyCard(Player player, OpportunityCard card) {
        player.setCash(player.getCash() - card.getAmount());
    }

    /**
     * The game is over when the player's passive income is equal or bigger then monthly expenses
     */
    @Override
    public boolean checkWinCondition(Player player) {
        double passiveIncome = player.getAssets().stream()
                .mapToDouble(Asset::getDailyCashFlow)
                .sum();
        return passiveIncome >= player.getDailyExpenses();
    }

    private void initPlayer(Player player) {
        double salary = initializer.generateRandomSalary();
        player.setSalary(salary);
        player.setCash(initializer.generateRandomCash(salary));

        Set<Liability> liabilities = initializer.generateLiabilities(salary);
        liabilities.forEach(l -> l.setOwner(player));

        player.getLiabilities().addAll(liabilities);
        player.setDailyExpenses(liabilities.stream()
                .mapToDouble(Liability::getDailyPayment)
                .sum());
    }

    private void updateCash(Player player) {
        double dailyFlow = financeService.getDailyCashFlow(player);
        player.setCash(player.getCash() + dailyFlow);
    }
}
