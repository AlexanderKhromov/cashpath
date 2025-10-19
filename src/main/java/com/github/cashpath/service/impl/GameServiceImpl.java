package com.github.cashpath.service.impl;

import com.github.cashpath.model.entity.Asset;
import com.github.cashpath.model.entity.Game;
import com.github.cashpath.model.entity.OpportunityCard;
import com.github.cashpath.model.entity.Player;
import com.github.cashpath.repository.AssetRepository;
import com.github.cashpath.repository.GameRepository;
import com.github.cashpath.repository.OpportunityCardRepository;
import com.github.cashpath.repository.PlayerRepository;
import com.github.cashpath.service.GameService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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
        double cashFlow = current.getSalary() + passiveIncome - current.getMonthlyExpenses();
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
    public Game createGame() {
        Game game = new Game();
        game.setStatus(Game.GameStatus.ACTIVE);
        game.setPlayers(new ArrayList<>());
        gameRepository.save(game);
        return game;

    }
}
