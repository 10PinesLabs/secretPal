package com.tenPines.model;

public class UserForFrontend {

    public Long id;

    public Worker worker;

    public String userName;

    public Boolean admin;


    public UserForFrontend(User user, Boolean isAdmin){
        this.id = user.getId();
        this.worker = user.getWorker();
        this.userName = this.worker.getFullName();
        this.admin = isAdmin;
    }
}
