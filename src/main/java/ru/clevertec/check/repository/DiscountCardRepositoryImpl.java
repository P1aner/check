package ru.clevertec.check.repository;

import ru.clevertec.check.model.DiscountCard;
import ru.clevertec.check.repository.api.DiscountCardRepository;
import ru.clevertec.check.utils.CSVReader;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

import static ru.clevertec.check.config.AppConfig.CSV_DELIMITER;
import static ru.clevertec.check.config.AppConfig.PATH_TO_DISCOUNT_CARD_FILE;

public class DiscountCardRepositoryImpl implements DiscountCardRepository {
    private static final Logger logger = Logger.getLogger("ru.clevertec.check.CheckRunner");

    private final List<DiscountCard> discountCardList = new ArrayList<>();

    private static DiscountCardRepositoryImpl instance;

    private DiscountCardRepositoryImpl() {
    }

    public static DiscountCardRepositoryImpl getInstance() {
        if (instance == null) {
            instance = new DiscountCardRepositoryImpl();
            instance.discountCardList.addAll(setUp());
        }
        return instance;
    }

    @Override
    public Optional<DiscountCard> findById(long id) {
        return discountCardList.stream()
                .filter(discountCard -> discountCard.getId().equals(id))
                .findFirst();
    }

    @Override
    public Optional<DiscountCard> findByNumber(Integer number) {
        return discountCardList.stream()
                .filter(discountCard -> discountCard.getNumber().equals(number))
                .findFirst();
    }

    private static List<DiscountCard> setUp() {
        List<List<String>> lists = new CSVReader().readFromCSV(PATH_TO_DISCOUNT_CARD_FILE, CSV_DELIMITER);
        List<DiscountCard> discountCards = new ArrayList<>();
        for (int i = 1; i < lists.size(); i++) {
            List<String> list = lists.get(i);
            try {
                discountCards.add(new DiscountCard(Long.parseLong(list.get(0)),
                        Integer.parseInt(list.get(1)),
                        Short.parseShort(list.get(2))));
            } catch (Exception e) {
                logger.warning("INTERNAL SERVER ERROR");
                throw new RuntimeException();
            }
        }
        return discountCards;
    }
}