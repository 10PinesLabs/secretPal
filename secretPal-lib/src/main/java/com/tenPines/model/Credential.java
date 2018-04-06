package com.tenPines.model;

/**
 * Created by Kevin on 19/10/16.
 */
public class Credential {

    private String userName;


    private String password;

    static public Credential newCredential(String aUserName, String aPassword) {
        Credential credential = new Credential();
        credential.setUserName(aUserName);
        credential.setPassword(aPassword);
        return credential;
    }

    public String getPassword() {
        return password;
    }

    public String getUserName() {
        return userName;
    }

    private void setUserName(String userName) {
        this.userName = userName;
    }

    private void setPassword(String password) {
        this.password = password;
    }
}
