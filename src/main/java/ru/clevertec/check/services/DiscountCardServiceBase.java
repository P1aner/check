package ru.clevertec.check.services;

import ru.clevertec.check.dto.DiscountCardDTO;
import ru.clevertec.check.dto.mapper.DiscountCardMapper;
import ru.clevertec.check.exception.ObjectNotFoundException;
import ru.clevertec.check.model.DiscountCard;
import ru.clevertec.check.repository.DiscountCardRepositorySqL;
import ru.clevertec.check.repository.api.DiscountCardRepository;

public class DiscountCardServiceBase {

    private final DiscountCardRepository discountCardRepository;
    private final DiscountCardMapper discountCardMapper;

    public DiscountCardServiceBase() {
        discountCardRepository = new DiscountCardRepositorySqL();
        discountCardMapper = new DiscountCardMapper();
    }

    public DiscountCardServiceBase(DiscountCardRepository discountCardRepository, DiscountCardMapper discountCardMapper) {
        this.discountCardRepository = discountCardRepository;
        this.discountCardMapper = discountCardMapper;
    }

    public void createNewDiscountCard(DiscountCardDTO discountCardDTO) {
        DiscountCard discountCard = discountCardMapper.discountCardDTOtoDiscountCard(discountCardDTO);
        discountCardRepository.save(discountCard);
    }

    public DiscountCardDTO getDiscountCard(String id) {
        long parsedId = Long.parseLong(id);
        DiscountCard discountCard = discountCardRepository.findById(parsedId)
                .orElseThrow(() -> new ObjectNotFoundException(String.format("Discount card with id: %s not found", id)));
        return discountCardMapper.discountCardtoDiscountCardDTO(discountCard);
    }

    public void updateDiscountCard(String id, DiscountCardDTO discountCardDTO) {
        long parsedId = Long.parseLong(id);
        if (discountCardRepository.exists(parsedId)) {
            DiscountCard discountCard = discountCardMapper.discountCardDTOtoDiscountCard(discountCardDTO);
            discountCard.setId(parsedId);
            discountCardRepository.save(discountCard);
        } else {
            throw new ObjectNotFoundException("Discount card %s not found".formatted(id));
        }
    }

    public void deleteDiscountCard(String id) {
        long parsedId = Long.parseLong(id);
        if (discountCardRepository.exists(parsedId)) {
            discountCardRepository.deleteById(parsedId);
        } else {
            throw new ObjectNotFoundException("Discount card %s not found".formatted(id));
        }
    }
}