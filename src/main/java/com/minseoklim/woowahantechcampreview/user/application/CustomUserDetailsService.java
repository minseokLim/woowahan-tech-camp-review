package com.minseoklim.woowahantechcampreview.user.application;

import static org.springframework.security.core.userdetails.User.*;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.minseoklim.woowahantechcampreview.user.domain.User;
import com.minseoklim.woowahantechcampreview.user.domain.repository.UserRepository;

@Service
@Transactional(readOnly = true)
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    public CustomUserDetailsService(final UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
        return userRepository.findByLoginId(username)
            .map(this::toUserDetails)
            .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));
    }

    private UserDetails toUserDetails(final User user) {
        return builder()
            .username(user.getId().toString())
            .password(user.getPassword())
            .authorities(user.getRoles())
            .disabled(user.isDeleted())
            .build();
    }
}
