package com.github.cashpath.controller.ui;

import com.github.cashpath.exception.GameNotFoundException;
import com.github.cashpath.model.entity.Game;
import com.github.cashpath.repository.GameRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.ArrayList;

@Controller
public class UIController {

    private final GameRepository gameRepository;

    public UIController(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @PostMapping("/create-game")
    public String createGame() {
        Game game = new Game();
        game.setPlayers(new ArrayList<>());
        gameRepository.save(game);
        return "redirect:/game/" + game.getId();
    }

    @GetMapping("/game/{id}")
    public String gamePage(@PathVariable Long id, Model model) {
        Game game = gameRepository.findById(id)
                .orElseThrow(() -> new GameNotFoundException(id));
        model.addAttribute("game", game);
        return "game";
    }
}