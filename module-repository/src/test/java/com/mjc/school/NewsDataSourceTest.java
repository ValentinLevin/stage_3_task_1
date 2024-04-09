package com.mjc.school;

import com.mjc.school.datasource.DataSource;
import com.mjc.school.datasource.DataSourceFactory;
import com.mjc.school.model.News;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class NewsDataSourceTest {
    private static final DataSource<News> dataSource = DataSourceFactory.getDataSource(News.class);
    private static final List<News> readItems = dataSource.findAll();

    @Test
    @DisplayName("Считаны все данные новостей")
    void readAllData() {
        List<Long> expectedIdList = List.of(1L, 2L, 3L);
        assertThat(readItems).hasSameSizeAs(expectedIdList);

        assertThat(readItems)
                .extracting("id")
                .containsExactlyElementsOf(expectedIdList);

        News expectedNews =
                new News(
                        1L,
                "News 1 title",
                "News 1 content",
                LocalDateTime.of(2024, 04, 05, 14, 12, 31),
                LocalDateTime.of(2024, 11, 02, 21, 36, 01),
                1L
                );

        Optional<News> actualNews = readItems.stream().filter(item -> item.getId() == 1L).findFirst();

        assertThat(actualNews).isPresent().contains(expectedNews);
    }
}
