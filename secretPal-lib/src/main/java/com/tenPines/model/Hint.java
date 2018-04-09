package com.tenPines.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Hint {
    @Id
    @GeneratedValue
    private Long id;

    private String message;

    public Hint() {
    }

    public Hint(String aMessage) {
        this.message = aMessage;
    }

    @Override
    public boolean equals(Object o) {

        if (o == this) return true;
        if (!(o instanceof Hint)) {
            return false;
        }

        Hint hint = (Hint) o;

        return this == hint;
    }

    public String message() {
        return message;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
