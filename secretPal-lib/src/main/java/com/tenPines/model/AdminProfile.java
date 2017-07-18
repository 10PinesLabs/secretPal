package com.tenPines.model;

import javax.persistence.*;

@Entity
@Table
public class AdminProfile {

    @OneToOne
    public User user;
    @Id
    @GeneratedValue
    private Long id;

    static public AdminProfile newAdmin(User anUser){
        AdminProfile adminProfile = new AdminProfile();
        adminProfile.setUser(anUser);
        return adminProfile;
    }

    public void setUser(User anUser){
        this.user = anUser;
    }

}
