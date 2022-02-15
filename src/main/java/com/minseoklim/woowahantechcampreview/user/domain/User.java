package com.minseoklim.woowahantechcampreview.user.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.minseoklim.woowahantechcampreview.common.BaseEntity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String loginId;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String nickName;

    @Column(nullable = false)
    private String email;

    public User(final String loginId, final String password, final String nickName, final String email) {
        this.loginId = loginId;
        this.password = password;
        this.nickName = nickName;
        this.email = email;
    }

    public void update(final User other) {
        this.loginId = other.getLoginId();
        this.password = other.getPassword();
        this.nickName = other.getNickName();
        this.email = other.getEmail();
    }
}
