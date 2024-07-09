package ru.clevertec.check.services.api;

import ru.clevertec.check.dto.DiscountCardDTO;

public interface DiscountCardService {

    void createNewDiscountCard(DiscountCardDTO discountCardDTO);

    DiscountCardDTO getDiscountCard(String id);

    void updateDiscountCard(String id, DiscountCardDTO discountCardDTO);

    void deleteDiscountCard(String id);
}
