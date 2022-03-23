package com.minseoklim.woowahantechcampreview.auth.filter;

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

import com.minseoklim.woowahantechcampreview.auth.domain.JwtTokenParser;

@Component
public class JwtFilter implements Filter {
    private static final String BEARER_TYPE = "Bearer";

    private final JwtTokenParser tokenParser;

    public JwtFilter(final JwtTokenParser tokenParser) {
        this.tokenParser = tokenParser;
    }

    @Override
    public void doFilter(
        final ServletRequest request,
        final ServletResponse response,
        final FilterChain chain
    ) throws ServletException, IOException {
        final HttpServletRequest httpServletRequest = (HttpServletRequest)request;
        final String token = resolveAccessToken(httpServletRequest);

        if (tokenParser.validateAccessToken(token)) {
            final Authentication authentication = tokenParser.extractAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        chain.doFilter(request, response);
    }

    private String resolveAccessToken(final HttpServletRequest request) {
        final String bearerToken = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (StringUtils.hasText(bearerToken) && bearerToken.toLowerCase().startsWith(BEARER_TYPE.toLowerCase())) {
            return bearerToken.substring(BEARER_TYPE.length()).trim();
        }
        return "";
    }
}
