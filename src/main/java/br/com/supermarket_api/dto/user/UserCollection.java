package br.com.supermarket_api.dto.user;

import java.util.List;

import org.springframework.data.domain.Page;

import br.com.supermarket_api.model.User;

public record UserCollection(List<UserResponse> items, Pagination pagination) {

    public record Pagination(int currentPage, int perPage, long total, int lastPage) {}

    public UserCollection(Page<User> page) {
        this(
            page.getContent().stream().map(UserResponse::from).toList(),
            new Pagination(
                page.getNumber() + 1,
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages()
            )
        );
    }
}