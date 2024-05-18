package com.mjc.school.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class GetNewsItemResponseDTO extends BaseResponseDTO {
    @JsonProperty("data")
    private final NewsDTO data;
}
