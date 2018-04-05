package com.tenPines.model;


import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;

@Entity
// We use the name usuario instead of user since 'user' is a keyword and
// jdbc doesn't quote table names and generates syntax error at runtime

// http://stackoverflow.com/questions/9035971/error-syntax-error-at-or-near-user
@Table(name="usuario")
public class User {

    @Id
    @GeneratedValue
    private Long id;

    @OneToOne
    public Worker worker;

    public Long backofficeId;

    public Worker getWorker() {
        return worker;
    }

    // Because hibernate
    private User () {}

    public User(Worker aWorker, Long backofficeId) {
        this.backofficeId = backofficeId;
        this.worker = aWorker;
    }

    public Long getId() {
        return id;
    }
}
