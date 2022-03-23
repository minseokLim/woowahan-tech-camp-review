package com.minseoklim.woowahantechcampreview.auth.domain;

import java.security.Key;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import com.minseoklim.woowahantechcampreview.auth.config.property.JwtProperty;

@Component
public class JwtTokenProvider {
    static final String AUTHORITIES_KEY = "auth";
    static final String AUTHORITY_DELIMITER = ",";
    static final String TOKEN_TYPE_KEY = "typ";

    private final Key secretKey;
    private final JwtProperty jwtProperty;

    @Autowired
    public JwtTokenProvider(final JwtProperty jwtProperty) {
        this.jwtProperty = jwtProperty;
        this.secretKey = Keys.hmacShaKeyFor(jwtProperty.getSecretKey().getBytes());
    }

    JwtTokenProvider(
        final String secretKey,
        final long accessTokenValidityInMilliseconds,
        final long refreshTokenValidityInMilliseconds
    ) {
        this(new JwtProperty(secretKey, accessTokenValidityInMilliseconds, refreshTokenValidityInMilliseconds));
    }

    public String createAccessToken(final Authentication authentication) {
        final Date now = new Date();
        final Date validity = new Date(now.getTime() + jwtProperty.getAccessTokenValidityInMilliseconds());

        return Jwts.builder()
            .setSubject(authentication.getName())
            .claim(AUTHORITIES_KEY, toString(authentication.getAuthorities()))
            .claim(TOKEN_TYPE_KEY, TokenType.ACCESS)
            .signWith(secretKey, SignatureAlgorithm.HS512)
            .setIssuedAt(now)
            .setExpiration(validity)
            .compact();
    }

    public String createRefreshToken() {
        final Date now = new Date();
        final Date validity = new Date(now.getTime() + jwtProperty.getRefreshTokenValidityInMilliseconds());

        return Jwts.builder()
            .claim(TOKEN_TYPE_KEY, TokenType.REFRESH)
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
