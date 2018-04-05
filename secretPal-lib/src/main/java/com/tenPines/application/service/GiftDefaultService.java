package com.tenPines.application.service;

import com.tenPines.model.DefaultGift;
import com.tenPines.persistence.GiftDefaultRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Aye on 02/11/16.
 */

@Service
public class GiftDefaultService {

    private final GiftDefaultRepository repository;

    public GiftDefaultService(GiftDefaultRepository repository) {
        this.repository = repository;
    }

    public List<DefaultGift> getAll() {
        return repository.findAll();

    }

    public void addGift(DefaultGift defaultGift) {
        List<DefaultGift> haveDefaultGift = repository.findAll();
        DefaultGift actualDefaultGift;
        actualDefaultGift = defaultGift;
        defaultGift.validateADefaultGift();
        if(!haveDefaultGift.isEmpty()){
            actualDefaultGift = haveDefaultGift.get(0);
            actualDefaultGift.changeDefaultGift(defaultGift);
        }

        repository.save(actualDefaultGift);
    }
}
