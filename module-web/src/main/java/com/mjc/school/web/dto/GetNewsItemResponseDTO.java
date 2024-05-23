package com.mjc.school.web.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mjc.school.service.dto.NewsDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GetNewsItemResponseDTO extends BaseResponseDTO {
    @JsonProperty("data")
    private NewsDTO data;

    @Override
    public int hashCode() {
        return Objects.hashCode(data);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (obj == this) {
            return true;
        }

        if (!(obj instanceof GetNewsItemResponseDTO o)) {
            return false;
        }

        return Objects.equals(data, o.data);
    }
}
