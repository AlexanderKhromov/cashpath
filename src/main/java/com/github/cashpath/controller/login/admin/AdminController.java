package com.github.cashpath.controller.login.admin;

import com.github.cashpath.model.dto.DeletePositionsRequest;
import com.github.cashpath.service.admin.AdminService;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin")
@AllArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final AdminService adminService;

    @GetMapping
    public String adminHome(Model model) {
        model.addAttribute("games", adminService.findAllGames());
        model.addAttribute("players", adminService.findAllPlayers());
        return "admin/dashboard";
    }

    @PostMapping("/games/delete")
    @PreAuthorize("hasRole('ADMIN')")
    public String deleteGames(@RequestParam(required = false) List<Long> gameIds) {
        if (gameIds != null && !gameIds.isEmpty()) {
            adminService.deleteAllGamesByIds(gameIds);
        }
        return "redirect:/admin";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/players/{playerId}/positions/delete")
    public String deletePositions(
            @PathVariable Long playerId,
            @ModelAttribute DeletePositionsRequest request
    ) {
        adminService.deletePositions(playerId, request);
        return "redirect:/admin";
    }

}
