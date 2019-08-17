package com.app.security;

public interface SecurityConfig {

    String SECRET = "SecretKeyToGenJWTs";
    long EXPIRATION_TIME = 864_000_000;
    String TOKEN_PREFIX = "Bearer ";
    String HEADER_STRING = "Authorization";
    String SIGN_UP_URL = "/users/register";
}
