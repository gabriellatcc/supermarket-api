package br.com.supermarket_api.dto.user;

import java.time.LocalDateTime;
import java.util.UUID;

import br.com.supermarket_api.model.User;

public record UserResponse(
        UUID id,
        String name,
        String email,
        LocalDateTime createdAt,
        LocalDateTime lastUpdatedAt
) {
    public static UserResponse from(User user) {
        return new UserResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getCreatedAt(),
                user.getLastUpdatedAt()
        );
    }
}