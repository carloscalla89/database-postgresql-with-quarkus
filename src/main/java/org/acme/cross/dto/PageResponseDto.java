package org.acme.cross.dto;

import java.util.List;

// Usando Records de Java para que sea inmutable y limpio
public record PageResponseDto<R>(
        List<R> data,
        int currentPage,
        int limit,
        long totalElements,
        int totalPages
) {}
