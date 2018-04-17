package com.tenPines.persistence;

import com.tenPines.model.DefaultGif;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DefaultGifRepository extends JpaRepository<DefaultGif,Long> {
}
