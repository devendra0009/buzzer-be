package com.davendra.buzzer.dto.response;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageableResponse<T> {
    private T data;
    private int totalPages;
    private int pageNumber;
    private int pageSize;
    private long totalElements;
    private boolean isLastPage;
}
