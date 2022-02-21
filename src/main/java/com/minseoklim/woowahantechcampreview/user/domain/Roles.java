package com.minseoklim.woowahantechcampreview.user.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.ElementCollection;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;

import lombok.Getter;

@Embeddable
@Getter
public class Roles {
    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    private List<Role> roles = new ArrayList<>();

    void addRole(final Role role) {
        roles.add(role);
    }

    void deleteRole(final Role role) {
        roles.remove(role);
    }
}
