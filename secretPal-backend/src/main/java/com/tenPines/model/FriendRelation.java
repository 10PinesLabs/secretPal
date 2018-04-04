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

    @OneToMany
    private List<String> hints;

//    @OneToOne(fetch = FetchType.EAGER, cascade = {CascadeType.REFRESH})
//    public SecretPalEvent event;

    public FriendRelation(){}

    public FriendRelation(Worker participant, Worker giftReceiver)  {
        this.giftGiver = participant;
        this.giftReceiver = giftReceiver;
        this.hints = new ArrayList<String>();
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

    public void addHint(String pista) {
        assertLessThan3Hints();
        this.hints.add(pista);
    }

    public List<String> hints() {
        return this.hints;
    }

    private void assertLessThan3Hints() {
        if(this.hints.size() >= 3){
            throw new RuntimeException("Can not have more than 3 hints");
        }
    }

    public void removeHint(String pista) {
        this.hints.remove(pista);
    }

    public void editHint(String pista, String pista_nueva) {
        hints.set(hints.indexOf(pista), pista_nueva);
    }
}
