package ru.clevertec.check.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.clevertec.check.dto.DiscountCardDTO;
import ru.clevertec.check.dto.mapper.DiscountCardMapper;
import ru.clevertec.check.exception.ObjectNotFoundException;
import ru.clevertec.check.repository.api.DiscountCardRepository;
import ru.clevertec.check.services.api.DiscountCardService;

import java.util.Optional;

class DiscountCardServiceBaseTest {
    private final DiscountCardRepository discountCardRepository = Mockito.mock(DiscountCardRepository.class);
    private final DiscountCardService discountCardService = new DiscountCardServiceBase(discountCardRepository, new DiscountCardMapper());

    @Test
    void getDiscountCardNegativeCase() {
        Mockito.when(discountCardRepository.findById(1L)).thenReturn(Optional.empty());
        Assertions.assertThrows(ObjectNotFoundException.class, () -> discountCardService.getDiscountCard("1"));
    }

    @Test
    void updateDiscountCardPositiveCase() {
        Mockito.when(discountCardRepository.exists(1L)).thenReturn(true);
        discountCardService.updateDiscountCard("1", new DiscountCardDTO(1, (short) 1));
        Mockito.verify(discountCardRepository, Mockito.times(1)).save(Mockito.any());
    }

    @Test
    void updateDiscountCardNegativeCase() {
        Mockito.when(discountCardRepository.exists(1L)).thenReturn(false);
        Assertions.assertThrows(ObjectNotFoundException.class, () -> discountCardService.updateDiscountCard("1", null));

    }

    @Test
    void createNewDiscountCard() {
        discountCardService.createNewDiscountCard(new DiscountCardDTO(1, (short) 1));
        Mockito.verify(discountCardRepository, Mockito.times(1)).save(Mockito.any());
    }

    @Test
    void deleteDiscountCardPositiveCase() {
        Mockito.when(discountCardRepository.exists(1L)).thenReturn(true);
        discountCardService.deleteDiscountCard("1");
        Mockito.verify(discountCardRepository, Mockito.times(1)).deleteById(1L);
    }

    @Test
    void deleteDiscountCardNegativeCase() {
        Mockito.when(discountCardRepository.exists(1L)).thenReturn(false);
        Assertions.assertThrows(ObjectNotFoundException.class, () -> discountCardService.deleteDiscountCard("1"));
    }
}