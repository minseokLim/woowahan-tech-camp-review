package com.minseoklim.woowahantechcampreview.auth.domain;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    ADMIN, USER;

    private static final String ROLE_PREFIX = "ROLE_";

    public static Role of(final String role) {
        final String name = role.substring(ROLE_PREFIX.length());
        return Role.valueOf(name);
    }

    @Override
    public String getAuthority() {
        return ROLE_PREFIX + this.name();
    }
}
