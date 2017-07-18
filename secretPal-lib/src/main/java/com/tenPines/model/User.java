package com.tenPines.model;


import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;

@Entity
// We use the name usuario instead of user since 'user' is a keyword and
// jdbc doesn't quote table names and generates syntax error at runtime

// http://stackoverflow.com/questions/9035971/error-syntax-error-at-or-near-user
@Table(name="usuario")
public class User {

    @OneToOne
    public Worker worker;
    @NotEmpty
    public String userName;
    @NotEmpty
    public String password;
    @Id
    @GeneratedValue
    private Long id;

    static public User newUser(Worker aWorker, String aUserName, String aPassword) {
        User user = new User();
        user.setWorker(aWorker);
        user.setUserName(aUserName);
        user.setPassword(aPassword);
        return user;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Worker getWorker() {
        return worker;
    }

    public void setWorker(Worker worker) {
        this.worker = worker;
    }

    public String geteMail(){
        return worker.geteMail();
    }
}