package ru.clevertec.check.services.data;

import ru.clevertec.check.model.DiscountCard;

public class DiscountCardTestData {

    public static DiscountCard getDiscountCardWithThreePercent() {
        return new DiscountCard(1, (short) 3);
    }

    public static DiscountCard getDiscountCardWithTwoPercent() {
        return new DiscountCard(1, (short) 2);
    }
}
