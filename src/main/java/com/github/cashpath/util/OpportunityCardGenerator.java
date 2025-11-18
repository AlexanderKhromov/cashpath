package com.github.cashpath.util;

import com.github.cashpath.model.entity.Asset;
import com.github.cashpath.model.entity.OpportunityCard;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class OpportunityCardGenerator {
    private static final Random RANDOM = new Random();

    private static final int CARD_COUNT = 100;

    // SMALL DEAL constants
    private static final double SMALL_MIN_PRICE = 5_000;
    private static final double SMALL_MAX_PRICE = 30_000;
    private static final double SMALL_MIN_ROI = 0.02;
    private static final double SMALL_MAX_ROI = 0.08;

    // BIG DEAL constants
    private static final double BIG_MIN_PRICE = 80_000;
    private static final double BIG_MAX_PRICE = 300_000;
    private static final double BIG_MIN_ROI = 0.08;
    private static final double BIG_MAX_ROI = 0.20;

    // DOODAD constants
    private static final double DOODAD_MIN_COST = 3_000;
    private static final double DOODAD_MAX_COST = 40_000;

    // Weighted probabilities
    private static final double SMALL_WEIGHT = 0.55;
    private static final double DOODAD_WEIGHT = 0.30;
    private static final double BIG_WEIGHT = 0.15;

    private final static List<String> SMALL_ASSETS = List.of(
            "Акции стартапа 'EcoGrow'",
            "Аренда гаража",
            "Доля в кофейне",
            "Покупка торгового автомата",
            "Онлайн-курс с монетизацией",

            "Мини-склад для аренды",
            "Домашняя пекарня",
            "Мобильная кофейная точка",
            "Сдача склада под маркетплейс",
            "Покупка 3D-принтера для заказов",

            "Мини-ферма микрозелени",
            "Инвестирование в NFT-коллекцию",
            "Аренда парковочного места",
            "Сторисмейкер на аутсорс",
            "Покупка оборудования для фотостудии",

            "Автономная солнечная панель для аренды",
            "Инструменты для сдачи в прокат",
            "Мини-мастерская ремонта техники",
            "Дропшиппинг-магазин",
            "Создание шаблонов для маркетплейсов"
    );

    private final static List<String> BIG_ASSETS = List.of(
            "Бизнес 'Green Energy'",
            "Апарт-отель у моря",
            "Франшиза сети кофеен",
            "Сеть автомоек",
            "IT-компания по подписке",

            "Мини-завод по переработке пластика",
            "Логистический склад",
            "Сеть пунктов выдачи заказов",
            "Медицинская клиника",
            "VR-развлекательный центр",

            "Частная школа робототехники",
            "Франшиза пиццерии",
            "Производство экопродуктов",
            "Частный детский сад",
            "Локальный маркетплейс услуг",

            "Сеть коворкингов",
            "Компания по аренде электросамокатов",
            "Инвестиция в крупный IT-стартап",
            "Гостевой дом в туристическом районе",
            "Производство модульных домов"
    );

    private final static List<String> DOODADS = List.of(
            "Новый смартфон",
            "Отпуск на Мальдивах",
            "Игровой ПК",
            "Кожаный диван",
            "Крутой велосипед",
            "Курс по саморазвитию",
            "Празднование дня рождения",

            "Поломка стиральной машины",
            "Ремонт автомобиля",
            "Шопинг — новая одежда",
            "Фитнес годовой абонемент",
            "Ужин в дорогом ресторане",
            "Смарт-часы последней модели",
            "Покупка музыкального инструмента",

            "Подарки родственникам",
            "Новый телевизор",
            "Тур выходного дня",
            "Домашние питомцы (ветеринар/корм)",
            "Налоговый сбор",
            "Ремонт квартиры"
    );

    public static List<OpportunityCard> generateRandomCards() {
        List<OpportunityCard> cards = new ArrayList<>();

        for (int i = 0; i < CARD_COUNT; i++) {
            OpportunityCard.OpportunityType type = getRandomWeightedType();
            cards.add(generateCardByType(type));
        }

        return cards;
    }

    private static OpportunityCard generateCardByType(OpportunityCard.OpportunityType type) {
        return switch (type) {
            case SMALL_DEAL -> createSmallDeal();
            case BIG_DEAL -> createBigDeal();
            case DOODAD -> createDoodad();
        };
    }

    private static OpportunityCard createSmallDeal() {
        String name = randomElement(SMALL_ASSETS);

        Asset asset = createAsset(
                name,
                SMALL_MIN_PRICE, SMALL_MAX_PRICE,
                SMALL_MIN_ROI, SMALL_MAX_ROI,
                randomElement(new Asset.AssetType[]{Asset.AssetType.REAL_ESTATE, Asset.AssetType.STOCK})
        );
        return buildCard(
                OpportunityCard.OpportunityType.SMALL_DEAL,
                asset,
                asset.getPrice(),
                "Возможность инвестировать в: " + name
        );
    }


    private static OpportunityCard createBigDeal() {
        String name = randomElement(BIG_ASSETS);

        Asset asset = createAsset(
                name,
                BIG_MIN_PRICE, BIG_MAX_PRICE,
                BIG_MIN_ROI, BIG_MAX_ROI,
                Asset.AssetType.BUSINESS
        );

        return buildCard(
                OpportunityCard.OpportunityType.BIG_DEAL,
                asset,
                asset.getPrice(),
                "Крупная инвестиция: " + name
        );
    }

    private static OpportunityCard createDoodad() {
        String name = randomElement(DOODADS);
        double cost = randomRange(DOODAD_MIN_COST, DOODAD_MAX_COST);

        return buildCard(
                OpportunityCard.OpportunityType.DOODAD,
                null,
                cost,
                "Трата: " + name
        );
    }

    private static Asset createAsset(String name,
                                     double minPrice,
                                     double maxPrice,
                                     double minRoi,
                                     double maxRoi,
                                     Asset.AssetType type) {
        double price = randomRange(minPrice, maxPrice);
        double income = price * randomRange(minRoi, maxRoi);

        Asset asset = new Asset();
        asset.setName(name);
        asset.setPrice(price);
        asset.setMonthlyCashFlow(income / 12);
        asset.setType(type);

        return asset;
    }

    private static OpportunityCard buildCard(
            OpportunityCard.OpportunityType type,
            Asset asset,
            double amount,
            String description
    ) {
        OpportunityCard card = new OpportunityCard();
        card.setType(type);
        card.setAsset(asset);
        card.setAmount(amount);
        card.setDescription(description);
        return card;
    }
    // ---------------- Utility ---------------------

    private static double randomRange(double min, double max) {
        return min + RANDOM.nextDouble() * (max - min);
    }

    private static <T> T randomElement(List<T> list) {
        return list.get(RANDOM.nextInt(list.size()));
    }

    private static <T> T randomElement(T[] array) {
        return array[RANDOM.nextInt(array.length)];
    }

    private static OpportunityCard.OpportunityType getRandomWeightedType() {
        double r = RANDOM.nextDouble();

        if (r < SMALL_WEIGHT) return OpportunityCard.OpportunityType.SMALL_DEAL;
        if (r < SMALL_WEIGHT + DOODAD_WEIGHT) return OpportunityCard.OpportunityType.DOODAD;
        return OpportunityCard.OpportunityType.BIG_DEAL;
    }
}
