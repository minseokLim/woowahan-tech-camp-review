package com.minseoklim.woowahantechcampreview.user.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import com.minseoklim.woowahantechcampreview.auth.domain.Role;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserRoles {
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "user_id")
    private List<UserRole> values = new ArrayList<>();

    void addRole(final Role role) {
        values.add(new UserRole(role));
    }

    void deleteRole(final Role role) {
        values.removeIf(userRole -> userRole.equalsRole(role));
    }

    public List<Role> getRoles() {
        return values.stream()
            .map(UserRole::getRole)
            .collect(Collectors.toUnmodifiableList());
    }
}
