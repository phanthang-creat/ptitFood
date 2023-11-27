package com.server.ptitFood.security.Jwt;

import io.jsonwebtoken.*;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Collection;
import java.util.Date;

@Component
@Slf4j
public class JwtTokenProvider {
    private static final String AUTHORITIES_KEY = "roles";

    private final RedisTemplate<String, Object> redisTemplate;

    private RSAPrivateKey privateKey;

    private RSAPublicKey publicKey;

    public JwtTokenProvider(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @PostConstruct
    public void init() throws Exception {

        RSAKeyProvider rsaKeyProvider = new RSAKeyProvider();

        try {
            this.privateKey = rsaKeyProvider.getPrivateKey();

            this.publicKey = rsaKeyProvider.getPublicKey();

        } catch (Exception e) {
            throw new RuntimeException("Exception occurred while loading keystore", e);
        }
    }

    public String generateToken(Authentication authentication) {
        try {

            String username = authentication.getName();

            Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

            long now = (new Date()).getTime();

            String token = Jwts.builder()
                    .setSubject(username)
                    .claim(AUTHORITIES_KEY, authorities)
                    .signWith(SignatureAlgorithm.RS512, privateKey)
                    .setExpiration(new Date(now + 3600000))
                    .compact();

            System.out.println("Token: " + token);

            redisTemplate.opsForValue().set(username, publicKey);

            System.out.println("Redis: " + redisTemplate.opsForValue().get(username));

            return token;
        }
        catch (Exception e) {
            System.out.println("Exception occurred while loading keystore");
            throw new RuntimeException("Exception occurred while loading keystore", e);
        }
    }

    public Authentication getAuthentication(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(publicKey)
                .parseClaimsJws(token)
                .getBody();

        Collection<? extends GrantedAuthority> authorities =
                AuthorityUtils.commaSeparatedStringToAuthorityList((String) claims.get(AUTHORITIES_KEY));

        User principal = new User(claims.getSubject(), "", authorities);

        return new UsernamePasswordAuthenticationToken(principal, "", authorities);
    }

    public boolean validateToken(String token) {
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(publicKey).parseClaimsJws(token);
            return !claims.getBody().getExpiration().before(new Date());
        } catch (JwtException | IllegalArgumentException e) {
            log.error("Expired or invalid JWT token");
            return false;
        }
    }
}
