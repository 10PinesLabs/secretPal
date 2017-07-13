package com.tenPines.persistence;

import com.tenPines.model.User;
import com.tenPines.model.Worker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public interface UserRepository extends JpaRepository<User, Long>{

    List<User> findByUserName(String userName);

    Long deleteByWorker(Worker worker);

    User findByWorker(Worker worker);
}
