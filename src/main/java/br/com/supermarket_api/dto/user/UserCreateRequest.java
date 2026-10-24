package br.com.supermarket_api.dto.user;

import java.util.HashMap;
import java.util.Map;

import jakarta.validation.constraints.*;

public record UserCreateRequest(
        @NotBlank(message = "Name is required") String name,
        @NotBlank(message = "Password is required")
        @Size(min = 8, message = "Password must be at least 8 characters") String password
) {
    public Map<String, Object> validated() {
        Map<String, Object> data = new HashMap<>();
        data.put("name", name);
        data.put("password", password);
        return data;
    }
}