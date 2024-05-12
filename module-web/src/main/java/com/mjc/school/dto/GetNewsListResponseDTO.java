package com.mjc.school.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@RequiredArgsConstructor
public class GetNewsListResponseDTO extends BaseResponseDTO {
    private final List<NewsDTO> items;
    private final long start;
    private final long size;
    private final long totalItemCount;
}
