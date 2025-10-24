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
import java.util.Random;

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
    public void playerMove(Long gameId) {
        Game game = gameRepository.findById(gameId).orElseThrow(() -> new GameNotFoundException(gameId));
        List<Player> players = game.getPlayers();
        if (players.isEmpty()) throw new PlayerNotFoundException();

        Player current = players.get(game.getCurrentTurn());

        // Calculate passive income
        double passiveIncome = current.getAssets().stream()
                .mapToDouble(Asset::getMonthlyCashFlow).sum();

        //TODO logic is wrong
        // Give a random card
        List<OpportunityCard> cards = cardRepository.findAll();
        OpportunityCard card = cards.get(new Random().nextInt(cards.size()));

        log.info("The card was: {}. ", card.getDescription());

        // Simple decision: if type is SMALL_DEAL or BIG_DEAL, suggest to buy
        if (card.getType() == OpportunityCard.OpportunityType.SMALL_DEAL || card.getType() == OpportunityCard.OpportunityType.BIG_DEAL) {
            if (card.getAsset() != null && current.getCash() >= card.getAsset().getPrice()) {
                current.setCash(current.getCash() - card.getAsset().getPrice());
                card.getAsset().setOwner(current);
                assetRepository.save(card.getAsset());
                log.info("An asset was bought: {} за {}", card.getAsset().getName(), card.getAsset().getPrice());
            } else {
                log.info("Not enough money to buy.");
            }
        } else if (card.getType() == OpportunityCard.OpportunityType.DOODAD) {
            // spend money
            current.setCash(current.getCash() - card.getAmount());
            log.info("Doodad spend: {}", card.getAmount());
        }

        // Update the cash of a player taking into account salary, passiveIncome и expenses
        double cashFlow = Math.round((current.getSalary() + passiveIncome - current.getMonthlyExpenses()) / MONTH_DAYS);
        current.setCash(current.getCash() + cashFlow);
        log.info("CashFlow of this move: {}", cashFlow);

        playerRepository.save(current);

        if (current.getCash() < 0) {
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

        for (Player player : players) {
            RandomGeneratorUtil randomGeneratorUtil = new RandomGeneratorUtil();
            player.setSalary(randomGeneratorUtil.getSalary());
            player.setCash(randomGeneratorUtil.getCash());

            List<Liability> liabilities = randomGeneratorUtil.getLiabilities();
            liabilities.forEach(e -> e.setOwner(player));
            player.setLiabilities(liabilities);

            player.setMonthlyExpenses(liabilities.stream()
                    .mapToDouble(Liability::getMonthlyPayment).sum());
            game.addPlayer(player);
        }
        return gameRepository.save(game);
    }

    @Override
    public Game findById(Long id) {
        return gameRepository.findById(id).orElseThrow(() -> new GameNotFoundException(id));
    }
}
