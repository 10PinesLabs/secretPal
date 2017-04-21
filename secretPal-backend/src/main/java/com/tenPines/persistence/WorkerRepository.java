package com.tenPines.persistence;

import com.tenPines.model.Worker;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Transactional
public interface WorkerRepository extends JpaRepository<Worker, Long> {

    List<Worker> findByeMail(String email);

    List<Worker> findBywantsToParticipate(Boolean bool);

    Worker findByfullName(String token);

    List<Worker> findAll(Specification<Worker> spec);
}


