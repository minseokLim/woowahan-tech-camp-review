package com.minseoklim.woowahantechcampreview.user.domain;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByLoginId(final String loginId);

    Optional<User> findByLoginIdAndEmailAndDeleted(final String loginId, final String email, final boolean deleted);
}
