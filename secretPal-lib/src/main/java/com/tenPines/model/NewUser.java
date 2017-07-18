package com.tenPines.model;


public class NewUser {

    private String userName;
    private String password;
    private String email;

    public NewUser(){
    }


    public static NewUser createANewUser(String userName, String password, String email) {
        if(userName == "" | password == "" | email == "" ) {
            throw new RuntimeException(NewUser.errorMessageWhenAnyFieldIsNull());
        }
        NewUser user = new NewUser();
        user.setUserName(userName);
        user.setPassword(password);
        user.setEmail(email);
        return user;
    }

    private static String errorMessageWhenAnyFieldIsNull() {
        return "Any field is null, please check your input data";
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }



}
