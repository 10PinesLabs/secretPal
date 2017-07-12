package com.tenPines.application.service.validation.rule;

import com.tenPines.model.Worker;
import org.dom4j.rule.Rule;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

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
        return RuleValidator.validate(giver, receiver);
    }

    /*TODO: Poner esto en el validate de arriba
        @Override
    public Boolean validate(Worker giver, Worker receiver) {
        return friendRelationService.getByWorkerReceiver(giver)
                .map(relation -> !relation.getGiftGiver().equals(receiver))
                .orElse(true);
    }
     */

    public void changeRuleIntention() {
        setIsActive(!isActive);
    }

    public boolean isActive() {
        return isActive;
    }

    public void setIsActive(Boolean newState) {
        this.isActive = newState;
    }
}
