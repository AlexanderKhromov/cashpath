package com.github.cashpath.util;

import com.github.cashpath.model.entity.Liability;
import lombok.Getter;

import java.util.*;

@Getter
public class RandomGeneratorUtil {
    private static final List<String> GOODS = List.of("Телефон", "Телевизор", "Ноутбук", "Стиральная машина", "Диван");
    private final Random random = new Random();
    private final double salary;
    private final double cash;
    private final Set<Liability> liabilities;

    public RandomGeneratorUtil() {
        this.salary = generateRandomSalary();
        this.liabilities = generateRandomInitialLiabilities(salary);
        this.cash = salary * random.nextDouble(0.2, 0.7);
    }

    private double generateRandomSalary() {
        return 15000 + random.nextInt(45001);// 15000–60000
    }

    private double generateRandomPercent(double bound) {
        return random.nextDouble(bound) + 0.01; // ±11%
    }

    private Set<Liability> generateRandomInitialLiabilities(double salary) {
        Set<Liability> result = new HashSet<>();
        result.add(createLiability("Жилье", salary, 0.40));
        result.add(createLiability("Кредитная карта", salary, generateRandomPercent(0.2)));
        result.add(createLiability(getRandomItem(), salary, generateRandomPercent(0.1)));
        return result;
    }

    private Liability createLiability(String liabilityName, double salary, double percent) {
        Liability l1 = new Liability();
        l1.setName(liabilityName);
        l1.setMonthlyPayment(salary * percent);
        return l1;
    }

    private String getRandomItem() {
        return GOODS.get(random.nextInt(GOODS.size()));
    }

}
