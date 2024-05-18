package com.mjc.school.service;

import com.mjc.school.dto.EditNewsRequestDTO;
import com.mjc.school.exception.AuthorNotFoundException;
import com.mjc.school.exception.DTOValidationException;
import com.mjc.school.exception.EntityNotFoundException;
import com.mjc.school.exception.NewsNotFoundException;
import com.mjc.school.model.Author;
import com.mjc.school.model.News;
import com.mjc.school.repository.Repository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;

class NewsServiceUpdateNewsTest {
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
    @DisplayName("Checking the news data submitted for saving to the repository, generated on the basis of an incoming request")
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

        News newsAfterChange = new News(
                newsIdToChange,
                requestDTO.getTitle(),
                requestDTO.getContent(),
                newsBeforeChange.getCreateDate(),
                LocalDateTime.of(2024, 4, 16, 14, 37, 31),
                requestDTO.getAuthorId()
        );

        Mockito.when(authorRepository.existsById(requestDTO.getAuthorId())).thenReturn(true);
        Mockito.when(newsRepository.findById(newsIdToChange)).thenReturn(newsBeforeChange);
        Mockito.when(newsRepository.save(Mockito.any(News.class))).thenReturn(newsAfterChange);

        ArgumentCaptor<News> argumentCaptor = ArgumentCaptor.forClass(News.class);

        LocalDateTime updateDateTimeFrom = LocalDateTime.now();
        newsService.update(newsIdToChange, requestDTO);
        LocalDateTime updateDateTimeTo = LocalDateTime.now();

        Mockito.verify(newsRepository).save(argumentCaptor.capture());

        News actualNewsToSave = argumentCaptor.getValue();

        assertThat(actualNewsToSave.getLastUpdateDate()).isBetween(updateDateTimeFrom, updateDateTimeTo);
        newsAfterChange.setLastUpdateDate(actualNewsToSave.getLastUpdateDate());
        assertThat(actualNewsToSave).isEqualTo(newsAfterChange);
    }

    @Test
    @DisplayName("Cases of correctness of news data. No exception should be thrown")
    void correctData_noThrownExceptions() {
        Long newsIdForChange = 1L;

        EditNewsRequestDTO requestDTO = new EditNewsRequestDTO(
                "12345",
                "54321",
                1L
        );

        News newsBeforeChange = new News(
                newsIdForChange,
                "Start title",
                "Start content",
                LocalDateTime.of(2024, 4, 16, 14, 33, 3),
                null,
                3L
        );

        News newsAfterChange = new News(
                newsIdForChange,
                requestDTO.getTitle(),
                requestDTO.getContent(),
                newsBeforeChange.getCreateDate(),
                LocalDateTime.of(2024, 4, 16, 14, 37, 31),
                requestDTO.getAuthorId()
        );

        Mockito.when(authorRepository.existsById(requestDTO.getAuthorId())).thenReturn(true);
        Mockito.when(newsRepository.findById(newsIdForChange)).thenReturn(newsBeforeChange);
        Mockito.when(newsRepository.save(Mockito.any(News.class))).thenReturn(newsAfterChange);

        assertThatNoException().isThrownBy(() -> newsService.update(newsIdForChange, requestDTO));

        requestDTO.setTitle("123456789012345678901234567890");
        requestDTO.setContent(
                "1234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890" +
                "1234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890" +
                "1234567890123456789012345678901234567890123456789012345"
        );

        assertThatNoException().isThrownBy(() -> newsService.update(newsIdForChange, requestDTO));
    }

    @Test
    @DisplayName("If a news title is too short, a DTOValidationException will be thrown when calling the update method.")
    void update_titleTooShort_throwsDTOValidateException() {
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
    @DisplayName("If the news title is too long, a DTOValidationException will be thrown when calling the update method.")
    void update_titleTooLong_throwsDTOValidateException() {
        Long newsIdForUpdate = 1L;
        EditNewsRequestDTO requestDTO = new EditNewsRequestDTO(
                "1234567890123456789012345678901", // 31
                "News content",
                2L
        );
        Mockito.when(authorRepository.existsById(requestDTO.getAuthorId())).thenReturn(true);
        assertThatThrownBy(() -> newsService.update(newsIdForUpdate, requestDTO)).isInstanceOf(DTOValidationException.class);
    }

    @Test
    @DisplayName("If the news content is too long, a DTOValidationException will be thrown when calling the update method.")
    void update_contentTooLong_throwsDTOValidateException() {
        Long newsIdForUpdate = 1L;
        EditNewsRequestDTO requestDTO = new EditNewsRequestDTO(
                "1234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890"+ // 100 per line
                "1234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890"+
                "1234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890",
                "News content",
                2L
        );
        Mockito.when(authorRepository.existsById(requestDTO.getAuthorId())).thenReturn(true);
        assertThatThrownBy(() -> newsService.update(newsIdForUpdate, requestDTO)).isInstanceOf(DTOValidationException.class);
    }

    @Test
    @DisplayName("If a news content is too short, a DTOValidationException will be thrown when calling the update method.")
    void update_contentTooShort_throwsDTOValidateException() {
        Long newsIdForUpdate = 1L;
        EditNewsRequestDTO requestDTO = new EditNewsRequestDTO(
                "News title",
                "123",
                2L
        );
        Mockito.when(authorRepository.existsById(requestDTO.getAuthorId())).thenReturn(true);
        assertThatThrownBy(() -> newsService.update(newsIdForUpdate, requestDTO)).isInstanceOf(DTOValidationException.class);
    }

    @Test
    @DisplayName("When passing an incorrect id for a new author, an AuthorNotFoundException will be thrown")
    void update_notFoundNewAuthor_throwsDTOValidateException() {
        Long newsIdForUpdate = 1L;
        EditNewsRequestDTO requestDTO = new EditNewsRequestDTO(
                "News title",
                "News content",
                2L
        );
        Mockito.when(authorRepository.existsById(requestDTO.getAuthorId())).thenReturn(false);
        assertThatThrownBy(() -> newsService.update(newsIdForUpdate, requestDTO)).isInstanceOf(AuthorNotFoundException.class);
    }

    @Test
    @DisplayName("When passing the id of a non-existent news, a NewsNotFoundException will be thrown")
    void update_notFoundNewsById_throwsEntityNotFoundException() {
        Long newsIdForUpdate = 1L;
        EditNewsRequestDTO requestDTO = new EditNewsRequestDTO(
                "News title",
                "News content",
                2L
        );
        Mockito.when(authorRepository.existsById(requestDTO.getAuthorId())).thenReturn(true);
        Mockito.when(newsRepository.findById(newsIdForUpdate)).thenThrow(EntityNotFoundException.class);
        assertThatThrownBy(() -> newsService.update(newsIdForUpdate, requestDTO)).isInstanceOf(NewsNotFoundException.class);
    }
}
