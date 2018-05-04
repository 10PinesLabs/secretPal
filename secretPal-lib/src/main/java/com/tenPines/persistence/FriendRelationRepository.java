package com.tenPines.persistence;

import com.tenPines.model.FriendRelation;
import com.tenPines.model.Worker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional
public interface FriendRelationRepository extends JpaRepository<FriendRelation, Long> {

    Optional<FriendRelation> findByGiftReceiver(Worker unWorker);

    Optional<FriendRelation> findByGiftGiver(Worker unWorker);

    default void deleteAllRelations() {
        for (FriendRelation friendRelation : findAll()) {
            delete(friendRelation);
        }
    }

    Long deleteByGiftGiver(Worker worker);
}
