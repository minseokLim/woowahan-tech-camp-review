package com.minseoklim.woowahantechcampreview.user.domain;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

public enum Role {
    ADMIN, USER;

    private static final String ROLE_PREFIX = "ROLE_";

    public GrantedAuthority toGrantedAuthority() {
        return new SimpleGrantedAuthority(ROLE_PREFIX + this.name());
    }
}
