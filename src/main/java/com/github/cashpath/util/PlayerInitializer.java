package com.github.cashpath.util;

import com.github.cashpath.model.entity.Liability;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class PlayerInitializer {
    private static final List<String> GOODS = List.of("Телефон", "Телевизор", "Ноутбук", "Стиральная машина", "Диван");
    private final Random random = new Random();

    public double generateRandomSalary() {
        return 15000 + random.nextInt(45001);// 15000–60000
    }

    public double generateRandomCash(double salary) {
        return salary * random.nextDouble(0.2, 0.7);
    }

    public Set<Liability> generateLiabilities(double salary) {
        Set<Liability> result = new HashSet<>();
        result.add(createLiability("Жилье", salary, 0.40));
        result.add(createLiability("Кредитная карта", salary, getRandomPercent(0.2)));
        result.add(createLiability(getRandomGood(), salary, getRandomPercent(0.1)));
        return result;
    }

    private double getRandomPercent(double bound) {
        return random.nextDouble(bound) + 0.01; // ±11%
    }

    private String getRandomGood() {
        return GOODS.get(random.nextInt(GOODS.size()));
    }

    private Liability createLiability(String liabilityName, double salary, double percent) {
        Liability l1 = new Liability();
        l1.setName(liabilityName);
        l1.setDailyPayment(salary * percent);
        return l1;
    }

}
