package br.com.supermarket_api.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import br.com.supermarket_api.common.ApiException;
import br.com.supermarket_api.common.ApiResponse;
import br.com.supermarket_api.dto.user.UserCollection;
import br.com.supermarket_api.dto.user.UserCreateRequest;
import br.com.supermarket_api.dto.user.UserResponse;
import br.com.supermarket_api.dto.user.UserUpdateRequest;
import br.com.supermarket_api.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<ApiResponse<UserCollection>> index(
            @RequestParam(required = false) String search,
            @RequestParam(name = "per_page", defaultValue = "15") int perPage) {
        try {
            Map<String, Object> data = new HashMap<>();
            data.put("search", search);
            data.put("per_page", perPage);

            return ApiResponse.success(userService.findUsers(data), "Users listed successfully!");
        } catch (ApiException e) {
            return ApiResponse.error("Error listing users: " + e.getMessage(), e.getStatus().value());
        } catch (Exception e) {
            return ApiResponse.error("Error listing users: " + e.getMessage(), 500);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResponse>> show(@PathVariable UUID id) {
        try {
            Map<String, Object> data = new HashMap<>();
            data.put("id", id);

            return ApiResponse.success(userService.findById(data), "User shown successfully!");
        } catch (ApiException e) {
            return ApiResponse.error("Error showing user: " + e.getMessage(), e.getStatus().value());
        } catch (Exception e) {
            return ApiResponse.error("Error showing user: " + e.getMessage(), 500);
        }
    }

    @PostMapping
    public ResponseEntity<ApiResponse<UserResponse>> store(@Valid @RequestBody UserCreateRequest request) {
        try {
            return ApiResponse.success(userService.createUser(request.validated()), "User created successfully!", 201);
        } catch (ApiException e) {
            return ApiResponse.error("Error creating user: " + e.getMessage(), e.getStatus().value());
        } catch (Exception e) {
            return ApiResponse.error("Error creating user: " + e.getMessage(), 500);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResponse>> update(
            @PathVariable UUID id,
            @Valid @RequestBody UserUpdateRequest request) {
        try {
            return ApiResponse.success(userService.updateUser(request.validated(id)), "User updated successfully!");
        } catch (ApiException e) {
            return ApiResponse.error("Error updating user: " + e.getMessage(), e.getStatus().value());
        } catch (Exception e) {
            return ApiResponse.error("Error updating user: " + e.getMessage(), 500);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable UUID id) {
        try {
            Map<String, Object> data = new HashMap<>();
            data.put("id", id);

            userService.deleteUser(data);
            return ApiResponse.<Void>success(null, "User deleted successfully!");
        } catch (ApiException e) {
            return ApiResponse.error("Error deleting user: " + e.getMessage(), e.getStatus().value());
        } catch (Exception e) {
            return ApiResponse.error("Error deleting user: " + e.getMessage(), 500);
        }
    }
}