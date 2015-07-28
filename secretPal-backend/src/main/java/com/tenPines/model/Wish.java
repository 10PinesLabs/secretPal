package com.tenPines.model;

import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table
public class Wish {

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.REFRESH})
    public Worker worker;
    @NotEmpty
    public String gift;
    @Id
    @GeneratedValue
    private Long id;

    public Wish() {
    }

    public Wish(Worker worker, String gift) {
        this.worker = worker;
        this.gift = gift;
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
