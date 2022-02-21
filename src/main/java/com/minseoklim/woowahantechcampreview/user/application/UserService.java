package com.minseoklim.woowahantechcampreview.user.application;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.minseoklim.woowahantechcampreview.common.exception.NotFoundException;
import com.minseoklim.woowahantechcampreview.user.domain.User;
import com.minseoklim.woowahantechcampreview.user.domain.UserRepository;
import com.minseoklim.woowahantechcampreview.user.dto.UserRequest;
import com.minseoklim.woowahantechcampreview.user.dto.UserResponse;

@Service
@Transactional
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(final UserRepository userRepository, final PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserResponse create(final UserRequest userRequest) {
        final User createdUser = userRepository.save(userRequest.toEntity(passwordEncoder));
        return UserResponse.of(createdUser);
    }

    @Transactional(readOnly = true)
    public Page<UserResponse> list(final Pageable pageable) {
        final Page<User> users = userRepository.findAll(pageable);
        return users.map(UserResponse::of);
    }

    @Transactional(readOnly = true)
    public UserResponse get(final Long id) {
        final User user = getUserById(id);
        return UserResponse.of(user);
    }

    public UserResponse update(final Long id, final UserRequest userRequest) {
        final User user = getUserById(id);
        user.update(userRequest.toEntity(passwordEncoder));
        return UserResponse.of(user);
    }

    public void delete(final Long id) {
        final User user = getUserById(id);
        user.delete();
    }

    private User getUserById(final Long id) {
        return userRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("사용자를 찾을 수 없습니다."));
    }
}
