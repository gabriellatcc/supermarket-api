package br.com.supermarket_api.common;

import java.util.List;
import java.util.function.Function;

import org.springframework.data.domain.Page;

public record PageResponse<T>(List<T> items, Pagination pagination) {
    public record Pagination(
        int currentPage,
        int perPage,
        long total,
        int lastPage
    ) {}

    public static <E, T> PageResponse<T> from(Page<E> page, Function<E, T> mapper) {
        return new PageResponse<>(
            page.getContent().stream().map(mapper).toList(),
            new Pagination(
                    page.getNumber() + 1,
                    page.getSize(),
                    page.getTotalElements(),
                    page.getTotalPages()
            )
        );
    }
}