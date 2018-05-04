package com.tenPines.restAPI;

public class SecurityToken {

    public SecurityToken(){
    }

    private void setToken(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    private String token;

    public static SecurityToken createWith(String aToken) {
        SecurityToken securityToken = new SecurityToken();
        securityToken.setToken(aToken);
        return securityToken;
    }
}
