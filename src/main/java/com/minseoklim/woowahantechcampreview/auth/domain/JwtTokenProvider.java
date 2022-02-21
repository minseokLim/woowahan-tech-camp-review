package com.minseoklim.woowahantechcampreview.auth.domain;

import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtTokenProvider {
    private static final String AUTHORITIES_KEY = "auth";
    private static final String AUTHORITY_DELIMITER = ",";

    private final Key secretKey;
    private final long validityInMilliseconds;
    private final JwtParser jwtParser;

    public JwtTokenProvider(
        @Value("${jwt.secret-key}") final String secretKey,
        @Value("${jwt.expire-length}") final long validityInMilliseconds
    ) {
        this.secretKey = Keys.hmacShaKeyFor(secretKey.getBytes());
        this.validityInMilliseconds = validityInMilliseconds;
        this.jwtParser = Jwts.parserBuilder().setSigningKey(this.secretKey).build();
    }

    public String createToken(final Authentication authentication) {
        final String authorities = authentication.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
            .collect(Collectors.joining(AUTHORITY_DELIMITER));

        final Date now = new Date();
        final Date validity = new Date(now.getTime() + this.validityInMilliseconds);

        return Jwts.builder()
            .setSubject(authentication.getName())
            .claim(AUTHORITIES_KEY, authorities)
            .signWith(secretKey, SignatureAlgorithm.HS512)
            .setIssuedAt(now)
            .setExpiration(validity)
            .compact();
    }

    public Authentication extractAuthentication(final String token) {
        final Claims claims = jwtParser.parseClaimsJws(token).getBody();
        final Collection<? extends GrantedAuthority> authorities =
            Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(AUTHORITY_DELIMITER))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
        final UserDetails principal = User.builder()
            .username(claims.getSubject())
            .password("N/A")
            .authorities(authorities)
            .build();

        return new UsernamePasswordAuthenticationToken(principal, token, authorities);
    }

    public boolean validateToken(final String token) {
        try {
            final Claims claims = jwtParser.parseClaimsJws(token).getBody();
            return !claims.getExpiration().before(new Date());
        } catch (final JwtException | IllegalArgumentException exception) {
            return false;
        }
    }
}
