package com.github.cashpath.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class GameDTO {
    private Long id;
    private int currentTurn;
    private LocalDate currentDay;
}
