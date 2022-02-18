package com.minseoklim.woowahantechcampreview.auth;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class JwtFilter implements Filter {
    private static final String BEARER_TYPE = "Bearer";

    private final JwtTokenProvider tokenProvider;

    public JwtFilter(final JwtTokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    @Override
    public void doFilter(
        final ServletRequest request,
        final ServletResponse response,
        final FilterChain chain
    ) throws ServletException, IOException {
        final HttpServletRequest httpServletRequest = (HttpServletRequest)request;
        final String token = resolveToken(httpServletRequest);

        if (tokenProvider.validateToken(token)) {
            final Authentication authentication = tokenProvider.extractAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        chain.doFilter(request, response);
    }

    private String resolveToken(final HttpServletRequest request) {
        final String bearerToken = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (StringUtils.hasText(bearerToken) && bearerToken.toLowerCase().startsWith(BEARER_TYPE.toLowerCase())) {
            return bearerToken.substring(BEARER_TYPE.length()).trim();
        }
        return null;
    }
}