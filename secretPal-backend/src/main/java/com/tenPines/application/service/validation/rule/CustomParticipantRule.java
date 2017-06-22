package com.tenPines.application.service.validation.rule;

import com.tenPines.model.Worker;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.*;

@Entity
@Table
public class CustomParticipantRule extends AssignationRule {

    @Id
    @GeneratedValue
    private Long id;

    @OneToOne
    public Worker _giver;

    @OneToOne
    public Worker _receiver;

    public CustomParticipantRule(Worker giver2, Worker receiver2) {
        _giver = giver2;
        _receiver = receiver2;
    }

    @Override
    public Boolean validate(Worker giver, Worker receiver) {
        return _giver.equals(giver) && _receiver.equals(receiver);
    }
}
