package com.minseoklim.woowahantechcampreview.user.ui;

import java.net.URI;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.minseoklim.woowahantechcampreview.auth.config.annotation.AuthenticatedUsername;
import com.minseoklim.woowahantechcampreview.user.application.UserService;
import com.minseoklim.woowahantechcampreview.user.dto.UserRequest;
import com.minseoklim.woowahantechcampreview.user.dto.UserResponse;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    public UserController(final UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<UserResponse> create(@Valid @RequestBody final UserRequest userRequest) {
        final UserResponse createdUser = userService.create(userRequest);
        final URI uri = URI.create("/users" + createdUser.getId());
        return ResponseEntity.created(uri).body(createdUser);
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponse> getMine(@AuthenticatedUsername final Long id) {
        final UserResponse user = userService.get(id);
        return ResponseEntity.ok(user);
    }

    @PutMapping("/me")
    public ResponseEntity<UserResponse> updateMine(
        @AuthenticatedUsername final Long id,
        @Valid @RequestBody final UserRequest userRequest
    ) {
        final UserResponse user = userService.update(id, userRequest);
        return ResponseEntity.ok(user);
    }

    @DeleteMapping("/me")
    public ResponseEntity<Void> deleteMine(@AuthenticatedUsername final Long id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
