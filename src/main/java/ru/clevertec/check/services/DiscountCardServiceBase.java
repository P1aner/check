package ru.clevertec.check.services;

import ru.clevertec.check.dto.DiscountCardDTO;
import ru.clevertec.check.dto.mapper.DiscountCardMapper;
import ru.clevertec.check.exception.CheckRunnerException;
import ru.clevertec.check.model.DiscountCard;
import ru.clevertec.check.repository.DiscountCardRepositorySqL;
import ru.clevertec.check.repository.api.DiscountCardRepository;

public class DiscountCardServiceBase {
    private final DiscountCardRepository discountCardRepository = new DiscountCardRepositorySqL();
    private final DiscountCardMapper discountCardMapper = new DiscountCardMapper();

    public void createNewDiscountCard(DiscountCardDTO discountCardDTO) {
        DiscountCard discountCard = discountCardMapper.discountCardDTOtoDiscountCard(discountCardDTO);
        discountCardRepository.save(discountCard);
    }

    public DiscountCardDTO getDiscountCard(String id) {
        long l = Long.parseLong(id);
        DiscountCard discountCard = discountCardRepository.findById(l)
                .orElseThrow(() -> new CheckRunnerException("INTERNAL SERVER ERROR"));
        return discountCardMapper.discountCardtoDiscountCardDTO(discountCard);
    }

    public void updateDiscountCard(DiscountCardDTO discountCardDTO, String id) {
        DiscountCard discountCard = discountCardMapper.discountCardDTOtoDiscountCard(discountCardDTO);
        long l = Long.parseLong(id);
        discountCard.setId(l);
        discountCardRepository.save(discountCard);
    }

    public void deleteDiscountCard(String id) {
        long l = Long.parseLong(id);
        discountCardRepository.deleteById(l);
    }
}