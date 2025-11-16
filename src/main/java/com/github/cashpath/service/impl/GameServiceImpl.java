package com.github.cashpath.service.impl;

import com.github.cashpath.exception.GameNotFoundException;
import com.github.cashpath.exception.OpportunityCardNotFoundException;
import com.github.cashpath.exception.PlayerNotFoundException;
import com.github.cashpath.model.dto.BuyRequestDTO;
import com.github.cashpath.model.dto.MoveResponseDTO;
import com.github.cashpath.model.entity.*;
import com.github.cashpath.model.mapper.MoveResponseMapper;
import com.github.cashpath.repository.*;
import com.github.cashpath.service.GameService;
import com.github.cashpath.util.PlayerInitializer;
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
    private final PlayerInitializer playerInitializer;

    @Override
    public Game getGame(Long gameId) {
        return gameRepository.findById(gameId).orElseThrow(() -> new GameNotFoundException(gameId));
    }

    @Transactional
    @Override
    public MoveResponseDTO buy(Long gameId, BuyRequestDTO request) {
        Game game = getGame(gameId);
        List<Player> players = game.getPlayers();
        if (players.isEmpty()) throw new PlayerNotFoundException();

        if (game.getCurrentTurn() >= players.size()) {
            throw new IllegalStateException("Invalid currentTurn index");
        }
        Player player = players.get(game.getCurrentTurn());

        Long cardId = request.cardId();
        OpportunityCard opportunityCardBought = cardRepository.findById(cardId).orElseThrow(() -> new OpportunityCardNotFoundException(cardId));

        double cardPrice = opportunityCardBought.getAmount();

        if (player.getCash() < cardPrice) {
            log.info("Player '{}' does not have enough cash to buy card: {}", player.getName(), opportunityCardBought.getDescription());
            switchTurn(game);
            return getMoveResponse(game);
        }
        //marking card as already bought
        opportunityCardBought.markAsUnavailable();
        opportunityCardBought = cardRepository.save(opportunityCardBought);

        switch (opportunityCardBought.getType()) {
            case SMALL_DEAL, BIG_DEAL -> {
                Asset asset = opportunityCardBought.getAsset();
                if (asset != null) {
                    asset.setOwner(player);
                    assetRepository.save(asset);
                    player.getAssets().add(asset);
                    log.info("The card was bought: {} by a player: {}", opportunityCardBought.getDescription(), player.getName());
                } else {
                    log.error("Something went wrong. DO investigation in code cause asset must be set for SMALL and BIG deals!");
                }
            }
            case DOODAD -> log.info("Player {} spent {} on DOODAD: {}", player.getName(), cardPrice, opportunityCardBought.getDescription());
            default -> log.warn("Unknown card type: {}", opportunityCardBought.getType());
        }

        double passiveIncome = getPassiveIncome(player);
        double dailyCashFlow = getDailyCashFlow(player);

        // Update current cash of the player
        double updatedCash = player.getCash() + dailyCashFlow - cardPrice;
        player.setCash(updatedCash);
        log.info("Daily cash flow for {}: {}", player.getName(), dailyCashFlow);

        playerRepository.save(player);

        if (passiveIncome >= player.getMonthlyExpenses()) {
            game.setStatus(Game.GameStatus.FINISHED);
            log.info("Game finished! Player {} won because has passive income {} ≥ monthly expenses {}",
                    player.getName(), passiveIncome, player.getMonthlyExpenses());
        }

        switchTurn(game);
        return getMoveResponse(game);
    }

    private double getPassiveIncome(Player player) {
        return player.getAssets().stream().mapToDouble(Asset::getMonthlyCashFlow).sum();
    }

    private double getDailyCashFlow(Player player) {
        double passiveIncome = getPassiveIncome(player);
        return Math.round((player.getSalary() + passiveIncome - player.getMonthlyExpenses()) / MONTH_DAYS);
    }

    @Transactional
    @Override
    public Game createGame(List<Player> players) {
        Game game = new Game();
        game.setStatus(Game.GameStatus.ACTIVE);
        game.setCurrentTurn(0);

        for (Player player : players) {
            double salary = playerInitializer.generateRandomSalary();
            player.setSalary(salary);
            player.setCash(playerInitializer.generateRandomCash(salary));
            Set<Liability> liabilities = playerInitializer.generateLiabilities(salary);

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
    public MoveResponseDTO endTurn(Long gameId) {
        Game game = getGame(gameId);
        switchTurn(game);
        return getMoveResponse(game);
    }

    private MoveResponseDTO getMoveResponse(Game game) {
        OpportunityCard nextCard = cardRepository.findRandomAvailableCard();
        Player currentPlayer = game.getPlayers().get(game.getCurrentTurn());
        return MoveResponseMapper.toMoveResponseDTO(game, currentPlayer.getName(), currentPlayer.getCash(),getDailyCashFlow(currentPlayer), nextCard);
    }

    private void switchTurn(Game game) {
        List<Player> players = game.getPlayers();
        if (!players.isEmpty()) {
            game.setCurrentTurn((game.getCurrentTurn() + 1) % players.size());
            game.setCurrentDay(game.getCurrentDay().plusDays(1));
            gameRepository.save(game);
        }
    }
}
