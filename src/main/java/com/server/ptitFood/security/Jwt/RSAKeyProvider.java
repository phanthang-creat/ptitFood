package com.server.ptitFood.security.Jwt;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Map;

@Data
@Component
public class RSAKeyProvider {
    private RSAPublicKey publicKey;
    private RSAPrivateKey privateKey;

    public RSAKeyProvider() throws Exception {

        Map<String, Object> keys = generateRSAKeys();

        this.publicKey = (RSAPublicKey) keys.get("public");

        this.privateKey = (RSAPrivateKey) keys.get("private");
    }

    private static Map<String, Object> generateRSAKeys() throws Exception {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        return Map.of("private", keyPair.getPrivate(), "public", keyPair.getPublic());
    }
}