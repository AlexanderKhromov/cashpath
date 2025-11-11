package com.github.cashpath.util;

import com.github.cashpath.model.entity.Asset;
import com.github.cashpath.model.entity.OpportunityCard;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class OpportunityCardGenerator {

    public static List<OpportunityCard> generateRandomCards() {
        List<OpportunityCard> cards = new ArrayList<>();
        Random random = new Random();

        List<String> smallAssets = List.of(
                "Акции стартапа 'EcoGrow'",
                "Аренда гаража",
                "Доля в кофейне",
                "Покупка торгового автомата",
                "Онлайн-курс с монетизацией"
        );

        List<String> bigAssets = List.of(
                "Бизнес 'Green Energy'",
                "Апарт-отель у моря",
                "Франшиза сети кофеен",
                "Сеть автомоек",
                "IT-компания по подписке"
        );

        List<String> doodads = List.of(
                "Новый смартфон",
                "Отпуск на Мальдивах",
                "Игровой ПК",
                "Кожаный диван",
                "Крутой велосипед",
                "Курс по саморазвитию",
                "Празднование дня рождения"
        );

        for (int i = 0; i < 100; i++) {
            OpportunityCard card = new OpportunityCard();
            OpportunityCard.OpportunityType type = OpportunityCard.OpportunityType.values()[random.nextInt(OpportunityCard.OpportunityType.values().length)];
            card.setType(type);

            switch (type) {
                case SMALL_DEAL -> {
                    String name = smallAssets.get(random.nextInt(smallAssets.size()));
                    double price = 5000 + random.nextDouble() * 15000;
                    double income = price * (0.05 + random.nextDouble() * 0.10);
                    Asset asset = new Asset();
                    asset.setName(name);
                    asset.setPrice(price);
                    asset.setMonthlyCashFlow(income / 12);
                    asset.setType(Asset.AssetType.values()[random.nextInt(Asset.AssetType.values().length)]);
                    card.setAsset(asset);
                    card.setAmount(price);
                    card.setDescription("Возможность инвестировать в: " + name);
                }
                case BIG_DEAL -> {
                    String name = bigAssets.get(random.nextInt(bigAssets.size()));
                    double price = 50000 + random.nextDouble() * 200000;
                    double income = price * (0.10 + random.nextDouble() * 0.15);
                    Asset asset = new Asset();
                    asset.setName(name);
                    asset.setPrice(price);
                    asset.setMonthlyCashFlow(income / 12);
                    asset.setType(Asset.AssetType.BUSINESS);
                    card.setAsset(asset);
                    card.setAmount(price);
                    card.setDescription("Крупная инвестиция: " + name);
                }
                case DOODAD -> {
                    String name = doodads.get(random.nextInt(doodads.size()));
                    double cost = 200 + random.nextDouble() * 10000;
                    card.setDescription("Трата: " + name);
                    card.setAmount(-cost);
                    card.setAsset(null);
                }
            }
            cards.add(card);
        }

        return cards;
    }
}
