package ru.clevertec.check.repository.api;

import java.util.Optional;

public interface GenericRepository<T> {
    Long save(T t);

    Optional<T> findById(long id);

    void deleteById(Long id);
}