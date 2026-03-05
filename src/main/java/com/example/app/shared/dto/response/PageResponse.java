package com.example.app.shared.dto.response;

import java.util.List;
import org.springframework.data.domain.Page;

public record PageResponse<T>(List<T> content, int pageSize, long totalCount) {
  public static <T> PageResponse<T> from(Page<T> page) {
    return new PageResponse<>(page.getContent(), page.getSize(), page.getTotalElements());
  }
}
