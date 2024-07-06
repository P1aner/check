package ru.clevertec.check.repository;

import ru.clevertec.check.exception.CheckRunnerException;
import ru.clevertec.check.model.DiscountCard;
import ru.clevertec.check.repository.api.DiscountCardRepository;
import ru.clevertec.check.utils.CsvUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static ru.clevertec.check.config.AppProperties.CSV_DELIMITER;
import static ru.clevertec.check.config.AppProperties.PATH_TO_DISCOUNT_CARD_FILE;

public class DiscountCardRepositoryCsV implements DiscountCardRepository {

    private final List<DiscountCard> discountCardList = new ArrayList<>();

    private static DiscountCardRepositoryCsV instance;

    private DiscountCardRepositoryCsV() {
    }

    public static DiscountCardRepositoryCsV getInstance() {
        if (instance == null) {
            instance = new DiscountCardRepositoryCsV();
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
        List<List<String>> lists = CsvUtil.readFromCSV(PATH_TO_DISCOUNT_CARD_FILE, CSV_DELIMITER);
        List<DiscountCard> discountCards = new ArrayList<>();
        try {
            for (int i = 1; i < lists.size(); i++) {
                List<String> list = lists.get(i);
                discountCards.add(buildDiscountCard(list));
            }
        } catch (Exception e) {
            throw new CheckRunnerException("INTERNAL SERVER ERROR");
        }
        return discountCards;
    }

    private static DiscountCard buildDiscountCard(List<String> list) {
        long id = Long.parseLong(list.get(0));
        int number = Integer.parseInt(list.get(1));
        short amount = Short.parseShort(list.get(2));
        return new DiscountCard(id, number, amount);
    }
}