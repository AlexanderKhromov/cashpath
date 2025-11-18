package com.github.cashpath.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class PlayerDTO {
    private Long id;
    private String name;
    private double cash;
    private double salary;
    double dailyCashFlow;
    private double monthlyExpenses;
    private double passiveIncome;
    private Set<LiabilityDTO> liabilities;
    private Set<AssetDTO> assets;
}
