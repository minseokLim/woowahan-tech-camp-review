package com.minseoklim.woowahantechcampreview.user.domain;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import com.minseoklim.woowahantechcampreview.auth.domain.Role;
import com.minseoklim.woowahantechcampreview.common.BaseEntity;
import com.minseoklim.woowahantechcampreview.common.exception.BadRequestException;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LoginId loginId;

    private Password password;

    private Name name;

    private Email email;

    private UserRoles userRoles = new UserRoles();

    private boolean deleted = false;

    public User(final String loginId, final Password password, final String name, final String email) {
        this.loginId = new LoginId(loginId);
        this.password = password;
        this.name = new Name(name);
        this.email = new Email(email);

        addRole(Role.USER);
    }

    public void update(final User other) {
        if (!loginId.equals(other.loginId)) {
            throw new BadRequestException("로그인 아이디는 수정할 수 없습니다.");
        }
        this.password = other.password;
        this.name = other.name;
        this.email = other.email;
    }

    public void delete() {
        this.deleted = true;
    }

    public void addRole(final Role role) {
        userRoles.addRole(role);
    }

    public void deleteRole(final Role role) {
        userRoles.deleteRole(role);
    }

    public void changePassword(final Password password) {
        this.password = password;
    }

    public Long getId() {
        return id;
    }

    public String getLoginId() {
        return loginId.getValue();
    }

    public String getPassword() {
        return password.getEncodedPassword();
    }

    public String getName() {
        return name.getValue();
    }

    public String getEmail() {
        return email.getValue();
    }

    public List<Role> getUserRoles() {
        return userRoles.getRoles();
    }

    public boolean isDeleted() {
        return deleted;
    }
}
