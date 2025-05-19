package com.slavacom.efectivemobiletesttask;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.util.Base64;

public class KeyGen {
    public static void main(String[] args) {
        Key key = Keys.secretKeyFor(SignatureAlgorithm.HS512);
        String base64Key = Base64.getEncoder().encodeToString(key.getEncoded());
        System.out.println(base64Key);
    }
}
