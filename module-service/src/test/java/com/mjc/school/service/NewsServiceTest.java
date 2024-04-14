package com.mjc.school.service;

import com.mjc.school.dto.AuthorDTO;
import com.mjc.school.dto.NewsDTO;
import com.mjc.school.model.Author;
import com.mjc.school.model.News;
import com.mjc.school.repository.Repository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class NewsServiceTest {
    private Repository<Author> authorRepository;
    private Repository<News> newsRepository;
    private NewsService newsService;

    @BeforeEach
    void setUp() {
        authorRepository = Mockito.mock(Repository.class);
        newsRepository = Mockito.mock(Repository.class);
        newsService = new NewsServiceImpl(newsRepository, authorRepository);
    }

    @Test
    @DisplayName("Checking the response to a single news request")
    void findById_exists_true() {
        Author author = new Author(1L, "Author 1 name");
        News news =
                new News(
                        1L,
                        "News 1 title",
                        "News 1 content",
                        LocalDateTime.of(2024, 04, 14, 17, 10, 12),
                        null,
                        1L
                );

        Mockito.doReturn(author).when(authorRepository).findById(1L);
        Mockito.doReturn(news).when(newsRepository).findById(1L);

        NewsDTO expectedNewsDTO =
                new NewsDTO(
                        news.getId(),
                        news.getTitle(),
                        news.getContent(),
                        "2024-04-14T17:10:12",
                        null,
                        new AuthorDTO(author.getId(), author.getName())
                );

        NewsDTO actualNewsDTO = newsService.findById(1L);

        assertThat(actualNewsDTO).isEqualTo(expectedNewsDTO);
    }

    @Test
    @DisplayName("If you specify an incorrect news id, null will be returned")
    void findById_exists_false() {
        Mockito.when(newsRepository.findById(1L)).thenReturn(null);
        NewsDTO actualNewsDTO = newsService.findById(1L);
        assertThat(actualNewsDTO).isNull();
    }

}
