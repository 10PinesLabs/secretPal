package com.tenPines.auth;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;

@Service
public class JWTGenerator {

    private final Algorithm key;

    public JWTGenerator(@Value("${jwt.secret}") String secret) throws UnsupportedEncodingException {
        key = Algorithm.HMAC256(secret);
    }

    public String encodeJWTFromString(String id) {
        return JWT.create()
                .withIssuer("secretPal")
                .withClaim("palId", id)
                .sign(key);
    }

    public String decodeJWTToString(String jwt) {
        return JWT.require(key)
                .withIssuer("secretPal")
                .build()
                .verify(jwt)
                .getClaim("palId")
                .asString();

    }
}
