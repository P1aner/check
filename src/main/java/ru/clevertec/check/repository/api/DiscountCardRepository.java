package ru.clevertec.check.repository.api;

import ru.clevertec.check.model.DiscountCard;

import java.util.Optional;

public interface DiscountCardRepository extends GenericRepository<DiscountCard> {
    Optional<DiscountCard> findByNumber(Integer number);
}