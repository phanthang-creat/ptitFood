package com.server.ptitFood.security.Jwt;

import io.jsonwebtoken.*;

import io.jsonwebtoken.impl.TextCodec;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import java.security.*;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtTokenProvider {
    private static final String AUTHORITIES_KEY = "roles";

    private String publicKey;

    private String privateKey;


    @PostConstruct
    public void init() {

        JwtConfig jwtConfig = new JwtConfig();

        try {
            publicKey = jwtConfig.getPublicKey();
            privateKey = jwtConfig.getPrivateKey();
        } catch (Exception e) {
            throw new RuntimeException("Exception occurred while loading keystore", e);
        }
    }

    public String generateToken(Authentication authentication) {
        try {

            System.out.println("Generate token");
            String username = authentication.getName();
            Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

            long now = (new Date()).getTime();

            Map<String, Object> keys = generateRSAKeys();

            System.out.println("Keys: " + keys);

            RSAPublicKey key = (RSAPublicKey) keys.get("public");

            PrivateKey privateKey = (PrivateKey) keys.get("private");

            String token = Jwts.builder()
                    .setSubject(username)
                    .claim(AUTHORITIES_KEY, authorities)
                    .signWith(SignatureAlgorithm.RS512, privateKey)
                    .setExpiration(new Date(now + 3600000))
                    .compact();

            System.out.println("Token: " + token);

            return Jwts.builder()
                    .setSubject(username)
                    .claim(AUTHORITIES_KEY, authorities)
                    .signWith(SignatureAlgorithm.RS512, privateKey)
                    .setExpiration(new Date(now + 3600000))
                    .compact();
        }
        catch (Exception e) {
            System.out.println("Exception occurred while loading keystore");
            System.out.println(e);
            throw new RuntimeException("Exception occurred while loading keystore", e);
        }
    }

    public Authentication getAuthentication(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(privateKey)
                .parseClaimsJws(token)
                .getBody();

        Collection<? extends GrantedAuthority> authorities =
                AuthorityUtils.commaSeparatedStringToAuthorityList((String) claims.get(AUTHORITIES_KEY));

        User principal = new User(claims.getSubject(), "", authorities);

        return new UsernamePasswordAuthenticationToken(principal, "", authorities);
    }

    public boolean validateToken(String token) {
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(privateKey).parseClaimsJws(token);
            return !claims.getBody().getExpiration().before(new Date());
        } catch (JwtException | IllegalArgumentException e) {
            log.error("Expired or invalid JWT token");
            return false;
        }
    }

    private static Map<String, Object> generateRSAKeys() throws Exception {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048);

        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        return Map.of("private", keyPair.getPrivate(), "public", keyPair.getPublic());
    }
}
