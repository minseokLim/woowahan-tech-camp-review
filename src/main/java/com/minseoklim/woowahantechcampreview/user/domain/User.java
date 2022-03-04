package com.minseoklim.woowahantechcampreview.user.domain;

import java.util.Collections;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import com.minseoklim.woowahantechcampreview.auth.domain.Role;
import com.minseoklim.woowahantechcampreview.common.BaseEntity;
import com.minseoklim.woowahantechcampreview.common.domain.EmailAddress;
import com.minseoklim.woowahantechcampreview.common.exception.BadRequestException;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private LoginId loginId;

    @Column(nullable = false)
    private Password password;

    @Column(nullable = false)
    private Name nickName;

    @Column(nullable = false)
    private EmailAddress email;

    @Embedded
    private Roles roles = new Roles();

    private boolean deleted = false;

    public User(final String loginId, final Password password, final String nickName, final String email) {
        this.loginId = new LoginId(loginId);
        this.password = password;
        this.nickName = new Name(nickName);
        this.email = new EmailAddress(email);

        addRole(Role.USER);
    }

    public void update(final User other) {
        if (!loginId.equals(other.loginId)) {
            throw new BadRequestException("로그인 아이디는 수정할 수 없습니다.");
        }
        this.password = other.password;
        this.nickName = other.nickName;
        this.email = other.email;
    }

    public void delete() {
        this.deleted = true;
    }

    public void addRole(final Role role) {
        roles.addRole(role);
    }

    public void deleteRole(final Role role) {
        roles.deleteRole(role);
    }

    public void changePassword(final Password password) {
        this.password = password;
    }

    public Long getId() {
        return id;
    }

    public String getLoginId() {
        return loginId.getLoginId();
    }

    public String getPassword() {
        return password.getPassword();
    }

    public String getNickName() {
        return nickName.getName();
    }

    public String getEmail() {
        return email.getEmailAddress();
    }

    public List<Role> getRoles() {
        return Collections.unmodifiableList(roles.getRoles());
    }

    public boolean isDeleted() {
        return deleted;
    }
}
