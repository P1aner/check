package ru.clevertec.check.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.clevertec.check.dto.DiscountCardDTO;
import ru.clevertec.check.dto.mapper.DiscountCardMapper;
import ru.clevertec.check.exception.ObjectNotFoundException;
import ru.clevertec.check.model.DiscountCard;
import ru.clevertec.check.repository.api.DiscountCardRepository;
import ru.clevertec.check.services.api.DiscountCardService;
import ru.clevertec.check.services.data.DiscountCardDTOTestData;
import ru.clevertec.check.services.data.DiscountCardTestData;

import java.util.Optional;

class DiscountCardServiceBaseTest {
    private static final String NUMBER = "1";

    private final DiscountCardRepository discountCardRepositoryMock = Mockito.mock(DiscountCardRepository.class);
    private final DiscountCardMapper discountCardMapperMock = Mockito.mock(DiscountCardMapper.class);
    private final DiscountCardService discountCardService = new DiscountCardServiceBase(discountCardRepositoryMock, discountCardMapperMock);

    @Test
    void getDiscountCardNegativeCase() {
        Mockito.when(discountCardRepositoryMock.findById(1L)).thenReturn(Optional.empty());

        Assertions.assertThrows(ObjectNotFoundException.class, () -> discountCardService.getDiscountCard(NUMBER));
    }

    @Test
    void updateDiscountCardPositiveCase() {
        DiscountCardDTO discountCardDTO = DiscountCardDTOTestData.getDiscountCardDTOWithThreePercent();
        DiscountCard discountCard = DiscountCardTestData.getDiscountCardWithThreePercent();

        Mockito.when(discountCardRepositoryMock.exists(1L)).thenReturn(true);
        Mockito.when(discountCardMapperMock.discountCardDTOtoDiscountCard(discountCardDTO)).thenReturn(discountCard);

        discountCardService.updateDiscountCard("1", discountCardDTO);
        Mockito.verify(discountCardRepositoryMock, Mockito.times(1)).save(Mockito.any());
    }

    @Test
    void updateDiscountCardNegativeCase() {
        DiscountCardDTO discountCardDTO = DiscountCardDTOTestData.getDiscountCardDTOWithThreePercent();

        Mockito.when(discountCardRepositoryMock.exists(1L)).thenReturn(false);

        Assertions.assertThrows(ObjectNotFoundException.class, () -> discountCardService.updateDiscountCard(NUMBER, discountCardDTO));
    }

    @Test
    void createNewDiscountCard() {
        DiscountCardDTO discountCardDTO = DiscountCardDTOTestData.getDiscountCardDTOWithThreePercent();
        DiscountCard discountCard = DiscountCardTestData.getDiscountCardWithThreePercent();

        Mockito.when(discountCardMapperMock.discountCardDTOtoDiscountCard(discountCardDTO)).thenReturn(discountCard);

        discountCardService.createNewDiscountCard(discountCardDTO);
        Mockito.verify(discountCardRepositoryMock, Mockito.times(1)).save(Mockito.any());
    }

    @Test
    void deleteDiscountCardPositiveCase() {
        Mockito.when(discountCardRepositoryMock.exists(1L)).thenReturn(true);

        discountCardService.deleteDiscountCard(NUMBER);
        Mockito.verify(discountCardRepositoryMock, Mockito.times(1)).deleteById(1L);
    }

    @Test
    void deleteDiscountCardNegativeCase() {
        Mockito.when(discountCardRepositoryMock.exists(1L)).thenReturn(false);

        Assertions.assertThrows(ObjectNotFoundException.class, () -> discountCardService.deleteDiscountCard(NUMBER));
    }
}