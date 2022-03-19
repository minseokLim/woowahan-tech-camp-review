package com.minseoklim.woowahantechcampreview.user.domain.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.minseoklim.woowahantechcampreview.user.domain.Email;
import com.minseoklim.woowahantechcampreview.user.domain.LoginId;
import com.minseoklim.woowahantechcampreview.user.domain.User;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByLoginId(final LoginId loginId);

    Optional<User> findByLoginIdAndEmailAndDeleted(
        final LoginId loginId,
        final Email email,
        final boolean deleted
    );
}