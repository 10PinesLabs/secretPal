package com.tenPines.model;

import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table
public class Wish {
    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    public Worker worker;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    public Worker createdBy;

    @NotEmpty
    public String gift;

    public Wish() {
    }

    public static Wish create (Worker createdBy, Worker worker, String gift) {
        Wish instance = new Wish();
        instance.createdBy = createdBy;
        instance.worker = worker;
        instance.gift = gift;
        return instance;
    }

    public Worker getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Worker createdBy) {
        this.createdBy = createdBy;
    }

    public Long getId() {
        return id;
    }

    public Worker getWorker() {
        return worker;
    }

    public void setWorker(Worker worker) {
        this.worker = worker;
    }

    public String getGift() {
        return gift;
    }

    public void setGift(String gift) {
        this.gift = gift;
    }
}
