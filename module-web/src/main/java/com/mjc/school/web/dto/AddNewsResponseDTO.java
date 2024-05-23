package com.mjc.school.web.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mjc.school.service.dto.NewsDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AddNewsResponseDTO extends BaseResponseDTO {
    @JsonProperty("data")
    private NewsDTO data;
    public AddNewsResponseDTO(NewsDTO data) {
        this.data = data;
    }
}
