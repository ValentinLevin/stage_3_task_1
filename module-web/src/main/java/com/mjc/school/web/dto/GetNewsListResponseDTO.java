package com.mjc.school.web.dto;

import com.mjc.school.service.dto.NewsDTO;
import lombok.*;

import java.util.List;
import java.util.Objects;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class GetNewsListResponseDTO extends BaseResponseDTO {
    private List<NewsDTO> items;
    private long start;
    private long size;
    private long totalItemCount;

    @Override
    public int hashCode() {
        return Objects.hash(items, start, size, totalItemCount);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (obj == this) {
            return true;
        }

        if (!(obj instanceof GetNewsListResponseDTO o)) {
            return false;
        }

        return Objects.equals(start, o.start)
                && Objects.equals(size, o.size)
                && Objects.equals(totalItemCount, o.totalItemCount)
                && Objects.equals(items, o.items);
    }
}
