package com.minseoklim.woowahantechcampreview.auth.util;

import static com.minseoklim.woowahantechcampreview.auth.util.JwtTokenProvider.*;

import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
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
import io.jsonwebtoken.security.Keys;

@Component
public class JwtTokenParser {
    private final JwtParser jwtParser;

    public JwtTokenParser(@Value("${jwt.secret-key}") final String secretKey) {
        final Key key = Keys.hmacShaKeyFor(secretKey.getBytes());
        this.jwtParser = Jwts.parserBuilder().setSigningKey(key).build();
    }

    public Authentication extractAuthentication(final String accessToken) {
        try {
            final Claims claims = jwtParser.parseClaimsJws(accessToken).getBody();
            final Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(AUTHORITY_DELIMITER))
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());
            final UserDetails principal = User.builder()
                .username(claims.getSubject())
                .password("N/A")
                .authorities(authorities)
                .build();

            return new UsernamePasswordAuthenticationToken(principal, accessToken, authorities);
        } catch (final JwtException | IllegalArgumentException | NullPointerException exception) {
            throw new BadCredentialsException(exception.getMessage());
        }
    }

    public boolean validateAccessToken(final String token) {
        return validateToken(token, false);
    }

    public boolean validateRefreshToken(final String token) {
        return validateToken(token, true);
    }

    private boolean validateToken(final String token, final boolean isRefreshToken) {
        try {
            final Claims claims = jwtParser.parseClaimsJws(token).getBody();
            final boolean additionalCondition = (claims.getSubject() != null) ^ isRefreshToken;
            return !claims.getExpiration().before(new Date()) && additionalCondition;
        } catch (final JwtException | IllegalArgumentException exception) {
            return false;
        }
    }
}
