package com.minseoklim.woowahantechcampreview.auth.domain.repository;

import org.springframework.data.repository.CrudRepository;

import com.minseoklim.woowahantechcampreview.auth.domain.RefreshToken;

public interface RefreshTokenRepository extends CrudRepository<RefreshToken, String> {
}
