package com.minseoklim.woowahantechcampreview.user.ui;

import com.minseoklim.woowahantechcampreview.user.application.UserService;
import com.minseoklim.woowahantechcampreview.user.dto.UserRequest;
import com.minseoklim.woowahantechcampreview.user.dto.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping
    public ResponseEntity<UserResponse> create(@RequestBody final UserRequest userRequest) {
        final UserResponse createdUser = userService.create(userRequest);
        final URI uri = URI.create("/users" + createdUser.getId());
        return ResponseEntity.created(uri).body(createdUser);
    }
}
