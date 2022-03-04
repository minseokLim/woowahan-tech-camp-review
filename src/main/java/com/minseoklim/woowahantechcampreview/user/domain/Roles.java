package com.minseoklim.woowahantechcampreview.user.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;

import lombok.Getter;

import com.minseoklim.woowahantechcampreview.auth.domain.Role;

@Embeddable
@Getter
public class Roles {
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_role")
    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private List<Role> values = new ArrayList<>();

    void addRole(final Role role) {
        values.add(role);
    }

    void deleteRole(final Role role) {
        values.remove(role);
    }
}
