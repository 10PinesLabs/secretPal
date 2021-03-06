package com.tenPines.application.service.validation.rule;

import com.tenPines.model.FriendRelation;
import com.tenPines.model.Worker;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
@Table
public class NotCircularRelationRule extends AssignationRule {

    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    public String description;
    @NotNull
    public boolean isActive;

    public NotCircularRelationRule() {
        this.description = "Si el Pino A le regala a el Pino B, el Pino B no le puede regalar a el Pino A.";
    }

    @Override
    public Boolean validate(Worker giver, Worker receiver) {
        return NotCircularRuleValidator.validate(this, giver, receiver);
    }

    public void changeRuleIntention() {
        setIsActive(!isActive);
    }

    public boolean isActive() {
        return isActive;
    }

    public void setIsActive(Boolean newState) {
        this.isActive = newState;
    }

    @Override
    public Boolean validate(FriendRelation relation, List<FriendRelation> newRelations) {
        return NotCircularRuleValidator.validate(this, relation, newRelations);
    }

}
