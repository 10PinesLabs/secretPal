package com.tenPines.persistence;

import java.util.List;

public interface AbstractRepository<T> {
    List<T> retrieveAll();

    void save(T... elements);

    T refresh(T element);

    void delete(T element);

    T findById(Long id);

    void update(T element);
}
