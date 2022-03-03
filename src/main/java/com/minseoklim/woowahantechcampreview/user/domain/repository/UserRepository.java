package com.minseoklim.woowahantechcampreview.user.domain.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.minseoklim.woowahantechcampreview.user.domain.User;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByLoginId(final String loginId);

    Optional<User> findByLoginIdAndEmailAndDeleted(final String loginId, final String email, final boolean deleted);
}
