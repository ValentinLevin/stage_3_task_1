package com.mjc.school.mapper;

import com.mjc.school.dto.AuthorDTO;
import com.mjc.school.dto.NewsDTO;
import com.mjc.school.model.News;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class NewsDTOMapperTest {

    @Test
    @DisplayName("Checking the correctness of entity to DTO conversion")
    void checkEntityToDTO() {
        News news =
                new News(
                        1L,
                        "News 1 title",
                        "New 1 content",
                        LocalDateTime.of(2024, 04, 14, 20, 43, 01),
                        LocalDateTime.of(2024, 05, 13, 21, 03, 31),
                        2L
                );

        NewsDTO expectedDTO =
                new NewsDTO(
                        1L,
                        "News 1 title",
                        "New 1 content",
                        "2024-04-14T20:43:01",
                        "2024-05-13T21:03:31",
                        new AuthorDTO(2L, null)
                );

        NewsDTO actualDTO = NewsDTOMapper.fromNews(news);

        assertThat(actualDTO).isEqualTo(expectedDTO);
    }

    @Test
    @DisplayName("Checking the correctness of entity with null lastUpdateDate to DTO conversion")
    void checkEntityToDTO_withNullLastUpdateDate() {
        News news =
                new News(
                        1L,
                        "News 1 title",
                        "New 1 content",
                        LocalDateTime.of(2024, 04, 14, 20, 43, 01),
                        null,
                        2L
                );

        NewsDTO expectedDTO =
                new NewsDTO(
                        1L,
                        "News 1 title",
                        "New 1 content",
                        "2024-04-14T20:43:01",
                        null,
                        new AuthorDTO(2L, null)
                );

        NewsDTO actualDTO = NewsDTOMapper.fromNews(news);

        assertThat(actualDTO).isEqualTo(expectedDTO);
    }

    @Test
    @DisplayName("Checking dto to entity conversion")
    void checkDTOToEntity() {
        NewsDTO newsDTO = new NewsDTO(
                1L,
                "News 1 title",
                "News 1 content",
                "2024-08-13T05:18:19",
                "2024-08-13T05:18:19",
                new AuthorDTO(1L, "Author name")
        );

        News expectedNews = new News(
                1L,
                "News 1 title",
                "News 1 content",
                null,
                null,
                1L
        );

        News actualNews = NewsDTOMapper.toNews(newsDTO);

        assertThat(actualNews).isEqualTo(expectedNews);
    }
}
