package com.minseoklim.woowahantechcampreview.user.domain;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

public enum Role {
    ROLE_ADMIN, ROLE_USER;

    public GrantedAuthority toGrantedAuthority() {
        return new SimpleGrantedAuthority(this.name());
    }
}
