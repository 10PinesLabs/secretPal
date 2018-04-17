package com.tenPines.application.service;

import com.tenPines.model.DefaultGif;
import com.tenPines.persistence.DefaultGifRepository;
import org.springframework.stereotype.Service;

@Service
public class DefaultGifService {

    private final DefaultGifRepository repository;

    public DefaultGifService(DefaultGifRepository repository) {
        this.repository = repository;
    }

    public void set(String newGifUrl){
        repository.deleteAll();
        repository.save(new DefaultGif(newGifUrl));
    }

    public DefaultGif get() {
        return repository.findAll().stream().findFirst()
                .orElse(new DefaultGif(defaultDefaultGifURL()));
    }

    public String defaultDefaultGifURL() {
        return "https://media.giphy.com/media/3oEhn78T277GKAq6Gc/giphy.gif";
    }
}
