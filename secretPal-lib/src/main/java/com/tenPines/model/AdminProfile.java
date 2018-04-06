package com.tenPines.model;

import javax.persistence.*;

@Entity
@Table
public class AdminProfile {

    @Id
    @GeneratedValue
    private Long id;

    @OneToOne
    public User user;

    static public AdminProfile newAdmin(User anUser){
        AdminProfile adminProfile = new AdminProfile();
        adminProfile.setUser(anUser);
        return adminProfile;
    }

    public void setUser(User anUser){
        this.user = anUser;
    }

}
