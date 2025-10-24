package com.github.cashpath.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Getter
@Setter
@Entity
@Table(name = "opportunity_cards")
public class OpportunityCard {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OpportunityType type;

    @Column(nullable = false)
    private double amount;

    @ManyToOne
    @JoinColumn(name = "asset_id")
    private Asset asset;

    //DOODAD means buying some product that does not bring you passive income
    public enum OpportunityType {
        SMALL_DEAL, BIG_DEAL, DOODAD
    }

    // ==================== GENERATOR ====================
    public static List<OpportunityCard> generateRandomCards() {
        List<OpportunityCard> cards = new ArrayList<>();
        Random random = new Random();

        // Assets by their types
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
            OpportunityType type = OpportunityType.values()[random.nextInt(OpportunityType.values().length)];
            card.setType(type);

            switch (type) {
                case SMALL_DEAL -> {
                    String name = smallAssets.get(random.nextInt(smallAssets.size()));
                    double price = 5000 + random.nextDouble() * 15000; // 5–20k
                    double income = price * (0.05 + random.nextDouble() * 0.10); // 5–15%
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
                    double price = 50000 + random.nextDouble() * 200000; // 50–250k
                    double income = price * (0.10 + random.nextDouble() * 0.15); // 10–25%
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
                    double cost = 200 + random.nextDouble() * 10000; // расходы 200–10k
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
