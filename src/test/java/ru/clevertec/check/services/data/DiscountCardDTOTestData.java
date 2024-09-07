package ru.clevertec.check.services.data;

import ru.clevertec.check.dto.DiscountCardDTO;

public class DiscountCardDTOTestData {

    public static DiscountCardDTO getDiscountCardDTOWithThreePercent() {
        return new DiscountCardDTO(1, (short) 3);
    }
}
