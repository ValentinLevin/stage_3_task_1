package com.mjc.school.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddNewsResponseDTO extends BaseResponseDTO {
    @JsonProperty("data")
    private final NewsDTO data;

    public AddNewsResponseDTO(NewsDTO data) {
        this.data = data;
    }
}
