package com.minseoklim.woowahantechcampreview.auth.config;

import java.util.List;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class AuthenticationUsernameConfig implements WebMvcConfigurer {
    private final AuthenticatedUsernameArgumentResolver argumentResolver;

    public AuthenticationUsernameConfig(final AuthenticatedUsernameArgumentResolver argumentResolver) {
        this.argumentResolver = argumentResolver;
    }

    @Override
    public void addArgumentResolvers(final List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(argumentResolver);
    }
}
