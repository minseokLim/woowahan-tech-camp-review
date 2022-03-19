package com.minseoklim.woowahantechcampreview.user.ui;

import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.minseoklim.woowahantechcampreview.auth.config.annotation.CheckAdminPermission;
import com.minseoklim.woowahantechcampreview.user.application.UserService;
import com.minseoklim.woowahantechcampreview.user.dto.RoleRequest;
import com.minseoklim.woowahantechcampreview.user.dto.UserRequest;
import com.minseoklim.woowahantechcampreview.user.dto.UserResponse;

@RestController
@CheckAdminPermission
@RequestMapping("/users")
public class UserAdminController {
    private final UserService userService;

    public UserAdminController(final UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<Page<UserResponse>> list(final Pageable pageable) {
        final Page<UserResponse> users = userService.list(pageable);
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> get(@PathVariable final Long id) {
        final UserResponse user = userService.get(id);
        return ResponseEntity.ok(user);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> update(
        @PathVariable final Long id,
        @Valid @RequestBody final UserRequest userRequest
    ) {
        final UserResponse user = userService.update(id, userRequest);
        return ResponseEntity.ok(user);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable final Long id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/roles")
    public ResponseEntity<UserResponse> addRole(
        @PathVariable final Long id,
        @Valid @RequestBody final RoleRequest roleRequest) {
        final UserResponse user = userService.addRole(id, roleRequest);
        return ResponseEntity.ok(user);
    }

    @DeleteMapping("/{id}/roles")
    public ResponseEntity<Void> deleteRole(
        @PathVariable final Long id,
        @Valid @RequestBody final RoleRequest roleRequest
    ) {
        userService.deleteRole(id, roleRequest);
        return ResponseEntity.noContent().build();
    }
}
