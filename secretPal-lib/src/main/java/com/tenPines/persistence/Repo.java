package com.tenPines.persistence;

import java.util.List;

public interface Repo<T> {
    List<T> retrieveAll();

    T save(T elements);

    T refresh(T element);

    void delete(T element);

    T findById(Long id);

    void update(T element);

    List<T> retrieveByCondition(String property,Object value);
}
