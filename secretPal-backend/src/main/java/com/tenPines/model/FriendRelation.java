package com.tenPines.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table
public class FriendRelation {


    @Id
    @GeneratedValue
    private Long id;

    @OneToOne
    private Worker giftGiver;

    @OneToOne
    private Worker giftReceiver;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name="friend_relation_id")
    private List<Hint> hints;

    public FriendRelation() {
    }

    public FriendRelation(Worker participant, Worker giftReceiver) {
        this.giftGiver = participant;
        this.giftReceiver = giftReceiver;
        this.hints = new ArrayList<Hint>();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Worker getGiftGiver() {
        return this.giftGiver;
    }

    public void setGiftGiver(Worker giftGiver) {
        this.giftGiver = giftGiver;
    }

    public Worker getGiftReceiver() {
        return this.giftReceiver;
    }

    public void setGiftReceiver(Worker giftReceiver) {
        this.giftReceiver = giftReceiver;
    }

    public void addHint(Hint pista) {
        assertLessThan3Hints();
        this.hints.add(pista);
    }

    public List<Hint> hints() {
        return this.hints;
    }

    private void assertLessThan3Hints() {
        if (this.hints.size() >= 3) {
            throw new RuntimeException("Can not have more than 3 hints");
        }
    }

    public void removeHint(Hint pista) {
        this.hints.remove(pista);
    }

    public void editHint(Hint pista, Hint pista_nueva) {
        hints.set(hints.indexOf(pista), pista_nueva);
    }

    public List<Hint> getHints() {
        return hints;
    }

    public void setHints(List<Hint> hints) {
        this.hints = hints;
    }
}
