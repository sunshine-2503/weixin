package com.shaoming.weixin.model;

/**
 * Created by jerry on 17-10-11.
 */
public class AccessToken {
    private String token;
    private String expiresIn;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(String expiresIn) {
        this.expiresIn = expiresIn;
    }

    @Override
    public String toString() {
        return "AccessToken{" +
                "token='" + token + '\'' +
                ", expiresIn='" + expiresIn + '\'' +
                '}';
    }
}
