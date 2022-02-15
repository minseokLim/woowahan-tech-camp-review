package com.minseoklim.woowahantechcampreview.user.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.minseoklim.woowahantechcampreview.user.domain.User;
import com.minseoklim.woowahantechcampreview.user.domain.UserRepository;
import com.minseoklim.woowahantechcampreview.user.dto.UserRequest;
import com.minseoklim.woowahantechcampreview.user.dto.UserResponse;

@Service
@Transactional
public class UserService {
    private final UserRepository userRepository;

    public UserService(final UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserResponse create(final UserRequest userRequest) {
        final User createdUser = userRepository.save(userRequest.toEntity());
        return UserResponse.of(createdUser);
    }
}
