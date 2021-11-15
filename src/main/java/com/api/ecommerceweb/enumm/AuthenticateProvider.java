package com.api.ecommerceweb.enumm;

public enum AuthenticateProvider {
    LOCAL(1), GOOGLE(2), FACEBOOK(3);

    int code;

     AuthenticateProvider(int code) {
        this.code = code;
    }
}
