package br.com.supermarket_api.dto.user;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import jakarta.validation.constraints.*;

public record UserUpdateRequest(
        @NotBlank(message = "Name is required") String name,
        @NotBlank(message = "Email is required") @Email(message = "Invalid email") String email
) {
    public Map<String, Object> validated(UUID id) {
        Map<String, Object> data = new HashMap<>();
        data.put("id", id);
        data.put("name", name);
        data.put("email", email);
        return data;
    }
}