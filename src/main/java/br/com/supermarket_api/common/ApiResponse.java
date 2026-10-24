package br.com.supermarket_api.common;

import org.springframework.http.ResponseEntity;

public record ApiResponse<T>(boolean success, T data, String message) {
    public static <T> ResponseEntity<ApiResponse<T>> respond(boolean success, T data, String message, int statusCode) {
        return ResponseEntity.status(statusCode)
        .body(new ApiResponse<>(success, data, message));
    }

    public static <T> ResponseEntity<ApiResponse<T>> success(T data, String message, int statusCode) {
        return respond(true, data, message, statusCode);
    }

    public static <T> ResponseEntity<ApiResponse<T>> success(T data, String message) {
        return respond(true, data, message, 200);
    }

    public static <T> ResponseEntity<ApiResponse<T>> success(T data) {
        return respond(
            true,
            data,
            "Operation completed successfully.", 200
        );
    }

    public static <T> ResponseEntity<ApiResponse<T>> error(String message, T data, int statusCode) {
        if (statusCode < 100 || statusCode > 599) {
            statusCode = 500;
        }
        return respond(false, data, message, statusCode);
    }

    public static <T> ResponseEntity<ApiResponse<T>> error(String message, int statusCode) {
        return error(message, null, statusCode);
    }
}