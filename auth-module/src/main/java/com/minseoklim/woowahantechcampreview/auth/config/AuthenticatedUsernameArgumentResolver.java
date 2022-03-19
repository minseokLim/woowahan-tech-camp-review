package com.minseoklim.woowahantechcampreview.auth.config;

import org.springframework.core.MethodParameter;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.minseoklim.woowahantechcampreview.auth.config.annotation.AuthenticatedUsername;

@Component
public class AuthenticatedUsernameArgumentResolver implements HandlerMethodArgumentResolver {
    @Override
    public boolean supportsParameter(final MethodParameter parameter) {
        return parameter.hasParameterAnnotation(AuthenticatedUsername.class);
    }

    @Override
    public Object resolveArgument(
        final MethodParameter parameter,
        final ModelAndViewContainer mavContainer,
        final NativeWebRequest webRequest,
        final WebDataBinderFactory binderFactory
    ) {
        final UserDetails principal = (UserDetails)SecurityContextHolder.getContext()
            .getAuthentication()
            .getPrincipal();
        return Long.parseLong(principal.getUsername());
    }
}
