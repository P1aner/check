package ru.clevertec.check.dto.mapper;

import ru.clevertec.check.dto.DiscountCardDTO;
import ru.clevertec.check.model.DiscountCard;

public class DiscountCardMapper {
    public DiscountCard discountCardDTOtoDiscountCard(DiscountCardDTO discountCardDTO) {
        short discountAmount = discountCardDTO.getDiscountAmount();
        int discountCardNumber = discountCardDTO.getDiscountCard();
        return new DiscountCard(discountCardNumber, discountAmount);
    }

    public DiscountCardDTO discountCardtoDiscountCardDTO(DiscountCard discountCard) {
        short discountAmount = discountCard.getAmount();
        int discountCardNumber = discountCard.getNumber();
        return new DiscountCardDTO(discountCardNumber, discountAmount);
    }
}
