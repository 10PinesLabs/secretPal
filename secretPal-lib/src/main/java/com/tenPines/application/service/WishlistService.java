package com.tenPines.application.service;

import com.tenPines.model.Wish;
import com.tenPines.model.Worker;
import com.tenPines.persistence.WishlistRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Aye on 07/11/16.
 */

@Service
public class WishlistService {

    private final WishlistRepository wishRepository;

    public WishlistService(WishlistRepository wishRepository) {
        this.wishRepository = wishRepository;
    }


    public List<Wish> retrieveAllWishes() {
        return wishRepository.findAll();
    }

    public Wish saveWish(Wish newWish) {
        return wishRepository.save(newWish);
    }

    public Wish retrieveAWish(Long id) {
        return wishRepository.findOne(id);
    }

    public void deleteAWish(Wish wish) {
        wishRepository.delete(wish);
    }

    public void updateWish(Wish wish) {
        wishRepository.save(wish);
    }

    public List<Wish> retrieveByWorker(Worker worker) {
        return wishRepository.findByWorker(worker);

    }
}
