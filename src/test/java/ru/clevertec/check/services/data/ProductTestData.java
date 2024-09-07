package ru.clevertec.check.services.data;

import ru.clevertec.check.model.Product;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class ProductTestData {

    public static Product getProductWithoutId() {
        return new Product("desc", BigDecimal.valueOf(10.10).setScale(2, RoundingMode.HALF_UP), 12, true);
    }

    public static Product getProductWithIdAndQuantitySmall() {
        return new Product(1L, "1", BigDecimal.valueOf(1.10).setScale(2, RoundingMode.HALF_UP), 3, true);
    }

    public static Product getProductWithIdAndQuantityMore1() {
        return new Product(1L, "1", BigDecimal.valueOf(1.10).setScale(2, RoundingMode.HALF_UP), 13, true);
    }

    public static Product getProductWithIdAndQuantityMore2() {
        return new Product(1L, "1", BigDecimal.valueOf(1.20).setScale(2, RoundingMode.HALF_UP), 10, true);
    }
}
