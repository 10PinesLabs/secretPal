package com.tenPines.persistence;

import com.tenPines.model.Hint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface HintsRepository extends JpaRepository<Hint, Long> {
}

