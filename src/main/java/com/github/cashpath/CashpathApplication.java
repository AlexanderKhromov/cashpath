package com.github.cashpath;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Gameplay
 * <p>
 * Players start with a base salary and cash.
 * <p>
 * Player's turn → random card/event.
 * <p>
 * The player decides whether to buy, skip, or sell the asset.
 * <p>
 * After the round, CashFlow is recalculated:
 * <p>
 * CashFlow = (salary + passiveIncome) - (expenses + liabilitiesPayments)
 * <p>
 * Players can enter the Fast Track when CashFlow >= target.
 * The game ends when everyone is on Fast Track or bankruptcy. ???
 */
@SpringBootApplication
public class CashpathApplication {

    public static void main(String[] args) {
        SpringApplication.run(CashpathApplication.class, args);
    }

}
