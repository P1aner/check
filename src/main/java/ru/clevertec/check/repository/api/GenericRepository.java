package ru.clevertec.check.repository.api;

import java.util.Optional;

public interface GenericRepository<T> {
    Optional<T> findById(long id);
}