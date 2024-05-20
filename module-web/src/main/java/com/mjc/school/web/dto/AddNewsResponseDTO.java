package com.mjc.school.web.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mjc.school.service.dto.NewsDTO;
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
