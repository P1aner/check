package ru.clevertec.check.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.clevertec.check.dto.DiscountCardDTO;
import ru.clevertec.check.dto.mapper.DiscountCardMapper;
import ru.clevertec.check.exception.CheckRunnerException;
import ru.clevertec.check.exception.ObjectNotFoundException;
import ru.clevertec.check.repository.api.DiscountCardRepository;
import ru.clevertec.check.services.api.DiscountCardService;

import java.util.Optional;

import static org.postgresql.hostchooser.HostRequirement.any;

class DiscountCardServiceBaseTest {
    private final DiscountCardRepository discountCardRepository = Mockito.mock(DiscountCardRepository.class);
    private final DiscountCardService discountCardService = new DiscountCardServiceBase(discountCardRepository, new DiscountCardMapper());

    @Test
    void getDiscountCardNegativeCase() {
        Mockito.when(discountCardRepository.findById(any.ordinal())).thenReturn(Optional.empty());
        CheckRunnerException thrown = Assertions.assertThrows(ObjectNotFoundException.class, () -> {
            discountCardService.getDiscountCard("1");
        });
        Assertions.assertEquals(ObjectNotFoundException.class, thrown.getClass());
    }

    @Test
    void updateDiscountCardNegativeCase() {
        Mockito.when(discountCardRepository.exists(any.ordinal())).thenReturn(false);
        CheckRunnerException thrown = Assertions.assertThrows(ObjectNotFoundException.class, () -> {
            discountCardService.updateDiscountCard("1", new DiscountCardDTO());
        });
        Assertions.assertEquals(ObjectNotFoundException.class, thrown.getClass());
    }

    @Test
    void deleteDiscountCardNegativeCase() {
        Mockito.when(discountCardRepository.exists(any.ordinal())).thenReturn(false);
        CheckRunnerException thrown = Assertions.assertThrows(ObjectNotFoundException.class, () -> {
            discountCardService.updateDiscountCard("1", new DiscountCardDTO());
        });
        Assertions.assertEquals(ObjectNotFoundException.class, thrown.getClass());
    }
}