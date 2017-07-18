package com.tenPines.restAPI;

public class SecurityToken {

    private String token;

    public SecurityToken() {
    }

    public static SecurityToken createWith(String aToken) {
        SecurityToken securityToken = new SecurityToken();
        securityToken.setToken(aToken);
        return securityToken;
    }

    public String getToken() {
        return token;
    }

    private void setToken(String token) {
        this.token = token;
    }
}
