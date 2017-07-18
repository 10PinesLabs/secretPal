package com.tenPines.persistence;

import com.tenPines.model.Wish;
import com.tenPines.model.Worker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public interface WishlistRepository extends JpaRepository<Wish, Long> {
    List<Wish> findByWorker(Worker worker);
}

