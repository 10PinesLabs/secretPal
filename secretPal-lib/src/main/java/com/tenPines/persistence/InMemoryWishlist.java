package com.tenPines.persistence;

import com.tenPines.model.Wish;

import java.util.ArrayList;
import java.util.List;

public class InMemoryWishlist implements Repo<Wish> {

    List<Wish> wishList = new ArrayList<>();

    @Override
    public List<Wish> retrieveAll() {
        return wishList;
    }

    @Override
    public Wish save(Wish wish) {
        wishList.add(wish);
        return wish;
    }

    @Override
    public Wish refresh(Wish wish) {
        return wish;
    }

    @Override
    public void delete(Wish wish) {
        wishList.remove(wish);
    }

    @Override
    public Wish findById(Long id) {
        return wishList.get(id.intValue());
    }

    @Override
    public void update(Wish wish) {
    }

    @Override
    public List<Wish> retrieveByCondition(String property, Object value) {
        return null;
    }
}
