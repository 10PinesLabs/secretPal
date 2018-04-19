package com.tenPines.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table
public class FriendRelation {

    public static final int HINTS_AMOUNT_LIMIT = 3;
    public static final int GUESS_ATTEMPTS_LIMIT = 3;
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

    private boolean isGuessed = false;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "guesses", joinColumns = @JoinColumn(name = "friend_relation_id"))
    @Column(name = "guess")
    private List<String> guesses;

    public FriendRelation() {
    }

    public FriendRelation(Worker participant, Worker giftReceiver) {
        this.giftGiver = participant;
        this.giftReceiver = giftReceiver;
        this.hints = new ArrayList<>();
        this.guesses = new ArrayList<>();
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

    public List<String> getGuessAttempts() {
        return guesses;
    }

    public int getAmountOfGuessAttempts() {
        return guesses.size();
    }

    public boolean isGuessed() {
        return isGuessed;
    }

    public void addHint(Hint aHint) {
        assertHintAmountLessThanLimit();
        this.hints.add(aHint);
    }

    public List<Hint> hints() {
        return this.hints;
    }

    private void assertHintAmountLessThanLimit() {
        if (this.hints.size() >= HINTS_AMOUNT_LIMIT) {
            throw new RuntimeException("Can not have more than "+ HINTS_AMOUNT_LIMIT+" hints");
        }
    }

    public void removeHint(Hint aHint) {
        this.hints.remove(aHint);
    }

    public void editHint(Hint anOldHint, Hint aNewHint) {
        hints.set(hints.indexOf(anOldHint), aNewHint);
    }

    public List<Hint> getHints() {
        return hints;
    }

    public void setHints(List<Hint> hints) {
        this.hints = hints;
    }

    public void guessGiftGiver(String guessedGiftGiverFullName) {
        assertIsNotGuessed();
        assertThereAreRemainingGuessAttempts();
        if(giftGiver.getFullName().equals(guessedGiftGiverFullName)){
            isGuessed = true;
        } else {
            guesses.add(guessedGiftGiverFullName);
        }
    }

    private void assertIsNotGuessed() {
        if (this.isGuessed) {
            throw new RuntimeException("The gift giver was already guessed");
        }
    }

    private void assertThereAreRemainingGuessAttempts() {
        if (this.guesses.size()  >= GUESS_ATTEMPTS_LIMIT) {
            throw new RuntimeException("Can not have more than "+ GUESS_ATTEMPTS_LIMIT +" failed guess attempts");
        }
    }

}
