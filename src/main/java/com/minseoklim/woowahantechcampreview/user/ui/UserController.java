package com.minseoklim.woowahantechcampreview.user.ui;

import java.net.URI;

import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.minseoklim.woowahantechcampreview.auth.config.annotation.AuthenticatedUsername;
import com.minseoklim.woowahantechcampreview.auth.config.annotation.CheckAdminPermission;
import com.minseoklim.woowahantechcampreview.user.application.UserService;
import com.minseoklim.woowahantechcampreview.user.dto.RoleRequest;
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

    @GetMapping
    @CheckAdminPermission
    public ResponseEntity<Page<UserResponse>> list(final Pageable pageable) {
        final Page<UserResponse> users = userService.list(pageable);
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    @CheckAdminPermission
    public ResponseEntity<UserResponse> get(@PathVariable final Long id) {
        final UserResponse user = userService.get(id);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponse> getMine(@AuthenticatedUsername final Long id) {
        final UserResponse user = userService.get(id);
        return ResponseEntity.ok(user);
    }

    @PutMapping("/{id}")
    @CheckAdminPermission
    public ResponseEntity<UserResponse> update(
        @PathVariable final Long id,
        @Valid @RequestBody final UserRequest userRequest
    ) {
        final UserResponse user = userService.update(id, userRequest);
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

    @DeleteMapping("/{id}")
    @CheckAdminPermission
    public ResponseEntity<Void> delete(@PathVariable final Long id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/me")
    public ResponseEntity<Void> deleteMine(@AuthenticatedUsername final Long id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/roles")
    @CheckAdminPermission
    public ResponseEntity<UserResponse> addRole(
        @PathVariable final Long id,
        @Valid @RequestBody final RoleRequest roleRequest) {
        final UserResponse user = userService.addRole(id, roleRequest);
        return ResponseEntity.ok(user);
    }

    @DeleteMapping("/{id}/roles")
    @CheckAdminPermission
    public ResponseEntity<Void> deleteRole(
        @PathVariable final Long id,
        @Valid @RequestBody final RoleRequest roleRequest
    ) {
        userService.deleteRole(id, roleRequest);
        return ResponseEntity.noContent().build();
    }
}
