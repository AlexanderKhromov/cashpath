package com.github.cashpath.controller.ui;

import com.github.cashpath.model.entity.Asset;
import com.github.cashpath.model.entity.Game;
import com.github.cashpath.model.entity.Player;
import com.github.cashpath.service.GameService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
@AllArgsConstructor
public class UIController {

    private final GameService gameService;

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @PostMapping("/create-game")
    public String createGame() {
        Player p1 = new Player();
        p1.setName("Игрок 1");
        Player p2 = new Player();
        p2.setName("Игрок 2");
        Game game = gameService.createGame(List.of(p1, p2));
        return "redirect:/game/" + game.getId();
    }

    @GetMapping("/game/{id}")
    public String gamePage(@PathVariable Long id, Model model) {
        Game game = gameService.findById(id);
        model.addAttribute("game", game);
        model.addAttribute("passiveIncomes", game.getPlayers().stream()
                .map(p -> p.getAssets().stream()
                        .mapToDouble(Asset::getMonthlyCashFlow).sum())
                .toList());
        return "game/game-board";
    }
    /*
    @PostMapping("/game/{id}/next-turn")
    public String nextTurn(@PathVariable Long id) {
        String log = gameService.playerMove(id);
        return "redirect:/game/" + id + "?log=" + log;
    }
     */
}