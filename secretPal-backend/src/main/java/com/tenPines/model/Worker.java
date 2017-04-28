package com.tenPines.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.tenPines.configuration.JsonDateDeserializer;
import com.tenPines.configuration.JsonDateSerializer;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Set;

@Entity
@Table
public class Worker {

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "worker")
    @JsonIgnore
    public Set<Wish> wish;
    @Id
    @GeneratedValue
    private Long id;
    @NotEmpty
    private String fullName;
    @NotEmpty
    @Email
    private String eMail;
    @JsonSerialize(using = JsonDateSerializer.class)
    @JsonDeserialize(using = JsonDateDeserializer.class)
    @NotNull
    private LocalDate dateOfBirth;
    @NotNull
    private Boolean wantsToParticipate;
    @JsonSerialize(using = JsonDateSerializer.class)
    @JsonDeserialize(using = JsonDateDeserializer.class)
    @Column
    private LocalDate giftDateReceived;

    // Necessary for hibernate
    private Worker() { }

    public Worker(String fullName, String email, LocalDate dateOfBirth, Boolean wantsToParticipate) {
        this.fullName = fullName;
        this.eMail = email;
        this.dateOfBirth = dateOfBirth;
        this.wantsToParticipate = wantsToParticipate;
    }

    public Set<Wish> getWish() {
        return wish;
    }

    public void setWish(Set<Wish> wish) {
        this.wish = wish;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void changeParticipationIntention() {
        setWantsToParticipate(!wantsToParticipate);
    }

    public boolean getWantsToParticipate() { return this.wantsToParticipate;}

    public void setWantsToParticipate(Boolean wantsToParticipate) {
        this.wantsToParticipate = wantsToParticipate;
    }

    public String geteMail() {
        return eMail;
    }

    public void seteMail(String eMail) throws Exception {
        this.eMail = eMail;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public LocalDate getGiftDateReceived() {
        return giftDateReceived;
    }

    public void setGiftDateReceived(LocalDate giftDateReceived) {
        this.giftDateReceived = giftDateReceived;
    }

    public void setDateOfBirth(LocalDate birthdayDate) {
        this.dateOfBirth = birthdayDate;
    }

    private void checkIfFieldIsValidUponCondition(Boolean condition, String message) throws Exception {
        if (condition) throw new Exception(message);
    }

    @Override
    public boolean equals(Object anObject) {
        if (this == anObject) return true;

        if (!(anObject instanceof Worker)) return false;
        Worker otherWorker = (Worker) anObject;

        return
                this.getFullName().equals(otherWorker.getFullName()) &&
                        this.geteMail().equals(otherWorker.geteMail()) &&
                        this.getDateOfBirth().equals(otherWorker.getDateOfBirth());
    }

    public void markGiftAsReceived() {
        setGiftDateReceived(LocalDate.now());
    }

}
