package com.github.cashpath.service.impl;

import com.github.cashpath.exception.GameNotFoundException;
import com.github.cashpath.exception.PlayerNotFoundException;
import com.github.cashpath.model.entity.*;
import com.github.cashpath.repository.*;
import com.github.cashpath.service.GameService;
import com.github.cashpath.util.RandomGeneratorUtil;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Transactional(readOnly = true)
@Service
@AllArgsConstructor
@Log4j2
public class GameServiceImpl implements GameService {
    public static final int MONTH_DAYS = 30;
    private final GameRepository gameRepository;
    private final PlayerRepository playerRepository;
    private final AssetRepository assetRepository;
    private final OpportunityCardRepository cardRepository;

    @Transactional
    @Override
    public void playerMove(Long gameId) {
        Game game = gameRepository.findWithPlayersAndAssetsById(gameId).orElseThrow(() -> new GameNotFoundException(gameId));
        List<Player> players = game.getPlayers();
        if (players.isEmpty()) throw new PlayerNotFoundException();

        Player player = players.get(game.getCurrentTurn());

        // Calculate passive income
        double passiveIncome = player.getAssets().stream()
                .mapToDouble(Asset::getMonthlyCashFlow).sum();

        OpportunityCard card = cardRepository.findRandomAvailableCard();
        log.info("The card was: {}. ", card.getDescription());

        // Simple decision: if type is SMALL_DEAL or BIG_DEAL, suggest to buy
        if (card.getType() == OpportunityCard.OpportunityType.SMALL_DEAL || card.getType() == OpportunityCard.OpportunityType.BIG_DEAL) {
            if (card.getAsset() != null && player.getCash() >= card.getAsset().getPrice()) {
                player.setCash(player.getCash() - card.getAsset().getPrice());
                card.getAsset().setOwner(player);
                assetRepository.save(card.getAsset());
                log.info("An asset was bought: {} за {}", card.getAsset().getName(), card.getAsset().getPrice());
            } else {
                log.info("Not enough money to buy.");
            }
        } else if (card.getType() == OpportunityCard.OpportunityType.DOODAD) {
            // spend money
            player.setCash(player.getCash() - card.getAmount());
            log.info("Doodad spend: {}", card.getAmount());
        }

        // Update the cash of a player taking into account salary, passiveIncome и expenses
        double cashFlow = Math.round((player.getSalary() + passiveIncome - player.getMonthlyExpenses()) / MONTH_DAYS);
        player.setCash(player.getCash() + cashFlow);
        log.info("CashFlow of this move: {}", cashFlow);

        playerRepository.save(player);

        if (player.getCash() < 0) {
            game.setStatus(Game.GameStatus.FINISHED);
            log.info("Player went bankrupt!");
        }
        // switch the move
        game.setCurrentTurn((game.getCurrentTurn() + 1) % players.size());
        game.setCurrentDay(game.getCurrentDay().plusDays(1));
        gameRepository.save(game);

    }

    @Transactional
    @Override
    public Game createGame(List<Player> players) {
        Game game = new Game();
        game.setStatus(Game.GameStatus.ACTIVE);
        //game.setCurrentDay(1);
        game.setCurrentTurn(0);

        for (Player player : players) {
            RandomGeneratorUtil randomGeneratorUtil = new RandomGeneratorUtil();
            player.setSalary(randomGeneratorUtil.getSalary());
            player.setCash(randomGeneratorUtil.getCash());

            Set<Liability> liabilities = randomGeneratorUtil.getLiabilities();
            liabilities.forEach(e -> e.setOwner(player));
            player.setLiabilities(liabilities);

            double monthlyExpenses = liabilities.stream()
                    .mapToDouble(Liability::getMonthlyPayment).sum();
            player.setMonthlyExpenses(monthlyExpenses);
            game.addPlayer(player);
        }
        return gameRepository.save(game);
    }

    @Override
    public Game findById(Long id) {
        return gameRepository.findById(id).orElseThrow(() -> new GameNotFoundException(id));
    }
}
