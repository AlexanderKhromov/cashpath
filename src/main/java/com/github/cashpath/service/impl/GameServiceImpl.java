package com.github.cashpath.service.impl;

import com.github.cashpath.exception.GameNotFoundException;
import com.github.cashpath.model.entity.*;
import com.github.cashpath.repository.*;
import com.github.cashpath.service.GameService;
import com.github.cashpath.util.RandomGeneratorUtil;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Random;

@Transactional(readOnly = true)
@Service
@AllArgsConstructor
public class GameServiceImpl implements GameService {
    private final GameRepository gameRepository;
    private final PlayerRepository playerRepository;
    private final AssetRepository assetRepository;
    private final OpportunityCardRepository cardRepository;

    public String playerMove(Long gameId) {
        Game game = gameRepository.findById(gameId).orElseThrow();
        List<Player> players = game.getPlayers();
        if (players.isEmpty()) return "There is no players in the game";

        Player current = players.get(game.getCurrentTurn());

        // Calculate passive income
        double passiveIncome = current.getAssets().stream()
                .mapToDouble(Asset::getMonthlyCashFlow).sum();

        // Give a random card
        List<OpportunityCard> cards = cardRepository.findAll();
        OpportunityCard card = cards.get(new Random().nextInt(cards.size()));

        String log = "The card was: " + card.getDescription() + ". ";

        // Simple decision: if type is SMALL_DEAL or BIG_DEAL, suggest to buy
        if (card.getType() == OpportunityCard.OpportunityType.SMALL_DEAL || card.getType() == OpportunityCard.OpportunityType.BIG_DEAL) {
            if (card.getAsset() != null && current.getCash() >= card.getAsset().getPrice()) {
                current.setCash(current.getCash() - card.getAsset().getPrice());
                card.getAsset().setOwner(current);
                assetRepository.save(card.getAsset());
                log += "An asset was bought: " + card.getAsset().getName() + " за " + card.getAsset().getPrice();
            } else {
                log += "Not enough money to buy.";
            }
        } else if (card.getType() == OpportunityCard.OpportunityType.DOODAD) {
            // spend money
            current.setCash(current.getCash() - card.getAmount());
            log += "Doodad spend: " + card.getAmount();
        }

        // Обновляем cash игрока с учетом salary, passiveIncome и expenses
        double cashFlow = (current.getSalary() + passiveIncome - current.getMonthlyExpenses()) / 30;
        current.setCash(current.getCash() + cashFlow);
        log += " | CashFlow of this move: " + cashFlow;

        playerRepository.save(current);

        // switch the move
        game.setCurrentTurn((game.getCurrentTurn() + 1) % players.size());
        gameRepository.save(game);

        return current.getName() + " moved. " + log;
    }

    @Transactional
    @Override
    public Game createGame(List<Player> players) {
        Game game = new Game();
        game.setStatus(Game.GameStatus.ACTIVE);

        for (Player player : players) {
            RandomGeneratorUtil randomGeneratorUtil = new RandomGeneratorUtil();
            player.setSalary(randomGeneratorUtil.getSalary());
            player.setCash(0);

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
