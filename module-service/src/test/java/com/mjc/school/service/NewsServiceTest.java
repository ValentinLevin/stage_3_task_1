package com.mjc.school.service;

import com.mjc.school.dto.AuthorDTO;
import com.mjc.school.dto.EditNewsRequestDTO;
import com.mjc.school.dto.NewsDTO;
import com.mjc.school.exception.DTOValidationException;
import com.mjc.school.exception.EntityNotFoundException;
import com.mjc.school.model.Author;
import com.mjc.school.model.News;
import com.mjc.school.repository.Repository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.tuple;

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

    @Test
    @DisplayName("Checking an object transferred for saving to the repository when calling the method to add news")
    void add_correctData() {
        EditNewsRequestDTO requestDTO = new EditNewsRequestDTO(
                "News title",
                "News content",
                2L
        );

        Mockito.when(authorRepository.existsById(2L)).thenReturn(true);

        ArgumentCaptor<News> argumentCaptor = ArgumentCaptor.forClass(News.class);

        LocalDateTime createDateFrom = LocalDateTime.now();
        newsService.add(requestDTO);
        LocalDateTime createDateTo = LocalDateTime.now();

        Mockito.verify(newsRepository).save(argumentCaptor.capture());

        News actualNews = argumentCaptor.getValue();

        assertThat(actualNews)
                .extracting("title", "content", "authorId")
                .containsOnly(requestDTO.getTitle(), requestDTO.getContent(), requestDTO.getAuthorId());

        assertThat(actualNews.getCreateDate()).isStrictlyBetween(createDateFrom, createDateTo);
    }

    @Test
    @DisplayName("Checking an object transferred for saving to the repository when calling the method to add news")
    void add_incorrectData_throwsDTOValidateException() {
        EditNewsRequestDTO requestDTO = new EditNewsRequestDTO(
                "12",
                "News content",
                2L
        );
        Mockito.when(authorRepository.existsById(2L)).thenReturn(true);
        assertThatThrownBy(() -> newsService.add(requestDTO)).isInstanceOf(DTOValidationException.class);
    }

    @Test
    @DisplayName("Проверка удаления новости по id")
    void deleteById_exists() {
        Long idForDelete = 1L;
        boolean expectedDeleteResult = true;

        Mockito.when(newsRepository.deleteById(idForDelete)).thenReturn(expectedDeleteResult);
        ArgumentCaptor<Long> argumentCaptor = ArgumentCaptor.forClass(Long.class);

        boolean actualDeleteResult = newsService.deleteById(idForDelete);

        Mockito.verify(newsRepository).deleteById(argumentCaptor.capture());

        Long actualId = argumentCaptor.getValue();

        assertThat(actualId).isEqualTo(idForDelete);
        assertThat(actualDeleteResult).isEqualTo(expectedDeleteResult);
    }

    @Test
    @DisplayName("Проверка передаваемой сущности для записи в репозиторий, сгенерированной на основании пришедшего запроса на изменение")
    void update_checkEntityToSave() {
        EditNewsRequestDTO requestDTO = new EditNewsRequestDTO(
                "Changed title",
                "Changed contend",
                1L
        );

        Long newsIdToChange = 2L;

        News newsBeforeChange = new News(
                newsIdToChange,
                "Start title",
                "Start content",
                LocalDateTime.of(2024, 4, 16, 14, 33, 3),
                null,
                3L
        );

        News expectedNews = new News(
                newsIdToChange,
                requestDTO.getTitle(),
                requestDTO.getContent(),
                newsBeforeChange.getCreateDate(),
                LocalDateTime.of(2024, 4, 16, 14, 37, 31),
                requestDTO.getAuthorId()
        );

        Mockito.when(newsRepository.findById(newsIdToChange)).thenReturn(newsBeforeChange);
        Mockito.when(authorRepository.existsById(requestDTO.getAuthorId())).thenReturn(true);
        Mockito.when(newsRepository.save(Mockito.any(News.class))).thenReturn(expectedNews);

        ArgumentCaptor<News> argumentCaptor = ArgumentCaptor.forClass(News.class);

        LocalDateTime updateDateTimeFrom = LocalDateTime.now();
        newsService.update(newsIdToChange, requestDTO);
        LocalDateTime updateDateTimeTo = LocalDateTime.now();

        Mockito.verify(newsRepository).save(argumentCaptor.capture());

        News actualNews = argumentCaptor.getValue();

        assertThat(actualNews.getLastUpdateDate()).isStrictlyBetween(updateDateTimeFrom, updateDateTimeTo);
        expectedNews.setLastUpdateDate(actualNews.getLastUpdateDate());
        assertThat(actualNews).isEqualTo(expectedNews);
    }

    @Test
    @DisplayName("Checking an object transferred for saving to the repository when calling the method to update news")
    void update_incorrectData_throwsDTOValidateException() {
        Long newsIdForUpdate = 1L;
        EditNewsRequestDTO requestDTO = new EditNewsRequestDTO(
                "12",
                "News content",
                2L
        );
        Mockito.when(authorRepository.existsById(requestDTO.getAuthorId())).thenReturn(true);
        assertThatThrownBy(() -> newsService.update(newsIdForUpdate, requestDTO)).isInstanceOf(DTOValidationException.class);
    }

    @Test
    @DisplayName("При передаче некорректного id нового автора будет выброшено исключение EntityNotFoundException")
    void update_notFoundNewAuthor_throwsDTOValidateException() {
        Long newsIdForUpdate = 1L;
        EditNewsRequestDTO requestDTO = new EditNewsRequestDTO(
                "News title",
                "News content",
                2L
        );
        Mockito.when(authorRepository.existsById(requestDTO.getAuthorId())).thenReturn(false);
        assertThatThrownBy(() -> newsService.update(newsIdForUpdate, requestDTO)).isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    @DisplayName("При передаче id несуществующей новости будет выброшено EntityNotFoundException")
    void update_notFoundNewsById_throwsEntityNotFoundException() {
        Long newsIdForUpdate = 1L;
        EditNewsRequestDTO requestDTO = new EditNewsRequestDTO(
                "News title",
                "News content",
                2L
        );
        Mockito.when(authorRepository.existsById(requestDTO.getAuthorId())).thenReturn(true);
        Mockito.when(newsRepository.findById(newsIdForUpdate)).thenReturn(null);
        assertThatThrownBy(() -> newsService.update(newsIdForUpdate, requestDTO)).isInstanceOf(EntityNotFoundException.class);
    }
}
