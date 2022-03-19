package com.minseoklim.woowahantechcampreview.user.domain.repository;

import org.springframework.data.repository.CrudRepository;

import com.minseoklim.woowahantechcampreview.user.domain.ResetPasswordToken;

public interface ResetPasswordTokenRepository extends CrudRepository<ResetPasswordToken, String> {
}
