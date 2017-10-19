package com.shaoming.core.util;

/**
 * Created by Jerry on 2017/7/18.
 */
public enum HttpRequestMethod {

    POST("post"),

    GET("get");

    private String value;

    HttpRequestMethod(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return this.value;
    }
}
