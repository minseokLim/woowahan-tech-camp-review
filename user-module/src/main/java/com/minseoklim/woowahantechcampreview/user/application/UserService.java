package com.minseoklim.woowahantechcampreview.user.application;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.minseoklim.woowahantechcampreview.auth.application.TokenService;
import com.minseoklim.woowahantechcampreview.common.exception.NotFoundException;
import com.minseoklim.woowahantechcampreview.user.domain.User;
import com.minseoklim.woowahantechcampreview.user.domain.repository.UserRepository;
import com.minseoklim.woowahantechcampreview.user.dto.RoleRequest;
import com.minseoklim.woowahantechcampreview.user.dto.UserRequest;
import com.minseoklim.woowahantechcampreview.user.dto.UserResponse;

@Service
@Transactional
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;

    public UserService(
        final UserRepository userRepository,
        final PasswordEncoder passwordEncoder,
        final TokenService tokenService
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenService = tokenService;
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

        tokenService.invalidateRefreshToken(id);
    }

    public UserResponse addRole(final Long id, final RoleRequest roleRequest) {
        final User user = getUserById(id);
        user.addRole(roleRequest.toRole());

        tokenService.invalidateRefreshToken(id);

        return UserResponse.of(user);
    }

    public void deleteRole(final Long id, final RoleRequest roleRequest) {
        final User user = getUserById(id);
        user.deleteRole(roleRequest.toRole());

        tokenService.invalidateRefreshToken(id);
    }

    private User getUserById(final Long id) {
        return userRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("???????????? ?????? ??? ????????????."));
    }
}
