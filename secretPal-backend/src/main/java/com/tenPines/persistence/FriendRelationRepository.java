package com.tenPines.persistence;

import com.tenPines.model.FriendRelation;
import com.tenPines.model.Worker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public interface FriendRelationRepository extends JpaRepository<FriendRelation, Long> {

    FriendRelation findByGiftReceiver(Worker unWorker);

    FriendRelation findByGiftGiver(Worker unWorker);

    default void deleteAllRelations() {
        for (FriendRelation friendRelation : findAll()) {
            delete(friendRelation);
        }

    }

    Long deleteByGiftGiver(Worker worker);
}
