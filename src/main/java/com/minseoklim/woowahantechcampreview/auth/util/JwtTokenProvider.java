package com.minseoklim.woowahantechcampreview.auth.util;

import java.security.Key;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtTokenProvider {
    static final String AUTHORITIES_KEY = "auth";
    static final String AUTHORITY_DELIMITER = ",";

    private final Key secretKey;
    private final long accessTokenValidityInMilliseconds;
    private final long refreshTokenValidityInMilliseconds;

    public JwtTokenProvider(
        @Value("${custom.jwt.secret-key}") final String secretKey,
        @Value("${custom.jwt.access-token-validity-in-milliseconds}") final long accessTokenValidityInMilliseconds,
        @Value("${custom.jwt.refresh-token-validity-in-milliseconds}") final long refreshTokenValidityInMilliseconds
    ) {
        this.secretKey = Keys.hmacShaKeyFor(secretKey.getBytes());
        this.accessTokenValidityInMilliseconds = accessTokenValidityInMilliseconds;
        this.refreshTokenValidityInMilliseconds = refreshTokenValidityInMilliseconds;
    }

    public String createAccessToken(final Authentication authentication) {
        final Date now = new Date();
        final Date validity = new Date(now.getTime() + accessTokenValidityInMilliseconds);

        return Jwts.builder()
            .setSubject(authentication.getName())
            .claim(AUTHORITIES_KEY, toString(authentication.getAuthorities()))
            .signWith(secretKey, SignatureAlgorithm.HS512)
            .setIssuedAt(now)
            .setExpiration(validity)
            .compact();
    }

    public String createRefreshToken() {
        final Date now = new Date();
        final Date validity = new Date(now.getTime() + refreshTokenValidityInMilliseconds);

        return Jwts.builder()
            .signWith(secretKey, SignatureAlgorithm.HS512)
            .setIssuedAt(now)
            .setExpiration(validity)
            .compact();
    }

    private static String toString(final Collection<? extends GrantedAuthority> grantedAuthorities) {
        return grantedAuthorities.stream()
            .map(GrantedAuthority::getAuthority)
            .collect(Collectors.joining(AUTHORITY_DELIMITER));
    }
}
