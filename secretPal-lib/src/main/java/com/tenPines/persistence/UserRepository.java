package com.tenPines.persistence;

import com.tenPines.model.User;
import com.tenPines.model.Worker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional
public interface UserRepository extends JpaRepository<User, Long>{

    Long deleteByWorker(Worker worker);

    User findByWorker(Worker worker);

    Optional<User> findByBackofficeId(Long backofficeId);
}
