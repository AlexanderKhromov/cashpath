package com.github.cashpath.util;

import com.github.cashpath.model.entity.Liability;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Getter
public class RandomGeneratorUtil {
    private static final List<String> GOODS = List.of("Телефон", "Телевизор", "Ноутбук", "Стиральная машина", "Диван");
    private final Random random = new Random();
    private final double salary;
    private final List<Liability> liabilities;

    public RandomGeneratorUtil() {
        this.salary = generateRandomSalary();
        this.liabilities = generateRandomInitialLiabilities(salary);
    }

    private double generateRandomSalary() {
        return 15000 + random.nextInt(45001);// 15000–60000
    }

    private double generateRandomMonthlyExpensePercent() {
        return random.nextDouble(0.1) + 0.01; // ±11%
    }

    private List<Liability> generateRandomInitialLiabilities(double salary) {
        List<Liability> result = new ArrayList<>();
        result.add(createLiability("Жилье", salary, 0.40));
        result.add(createLiability("Кредитная карта", salary, generateRandomMonthlyExpensePercent()));
        result.add(createLiability(getRandomItem(), salary, generateRandomMonthlyExpensePercent()));
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
