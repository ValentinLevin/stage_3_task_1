package com.mjc.school.service.service;

import com.mjc.school.repository.exception.EntityNotFoundException;
import com.mjc.school.repository.exception.EntityNullReferenceException;
import com.mjc.school.repository.exception.EntityValidationException;
import com.mjc.school.repository.exception.KeyNullReferenceException;
import com.mjc.school.service.dto.EditNewsRequestDTO;
import com.mjc.school.service.exception.AuthorNotFoundException;
import com.mjc.school.service.exception.DTOValidationException;
import com.mjc.school.repository.model.Author;
import com.mjc.school.repository.model.News;
import com.mjc.school.repository.repository.Repository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class NewsServiceAddNewsTest {
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
    @DisplayName("When the added news contains an author who is not in the list of authors, the method will throw an AuthorNotFoundException exception")
    void add_incorrectData_throwsAuthorNotExistsException() throws KeyNullReferenceException {
        EditNewsRequestDTO requestDTO = new EditNewsRequestDTO(
                "News title",
                "News content",
                2L
        );
        Mockito.when(authorRepository.existsById(2L)).thenReturn(false);
        assertThatThrownBy(() -> newsService.add(requestDTO)).isInstanceOf(AuthorNotFoundException.class);
    }
    @Test
    @DisplayName("Cases of correctness of news data. No exception should be thrown")
    void correctData_noThrownExceptions() throws KeyNullReferenceException, EntityNotFoundException, EntityNullReferenceException, EntityValidationException {
        EditNewsRequestDTO requestDTO = new EditNewsRequestDTO(
                "12345",
                "54321",
                1L
        );

        News addedNews = new News(
                1L,
                requestDTO.getTitle(),
                requestDTO.getContent(),
                LocalDateTime.of(2024, 4, 16, 14, 37, 31),
                LocalDateTime.of(2024, 4, 16, 14, 37, 31),
                requestDTO.getAuthorId()
        );

        Author author = new Author(
                1L, "Author name"
        );

        Mockito.when(authorRepository.existsById(requestDTO.getAuthorId())).thenReturn(true);
        Mockito.when(authorRepository.findById(author.getId())).thenReturn(author);

        Mockito.when(newsRepository.save(Mockito.any(News.class))).thenReturn(addedNews);
        Mockito.when(newsRepository.findById(addedNews.getId())).thenReturn(addedNews);

        LocalDateTime createdAtFrom = LocalDateTime.now();
        assertThatNoException().isThrownBy(() -> newsService.add(requestDTO));
        LocalDateTime createdAtTo= LocalDateTime.now();

        ArgumentCaptor<News> argumentCaptor = ArgumentCaptor.forClass(News.class);
        Mockito.verify(newsRepository).save(argumentCaptor.capture());

        News newsToSave = argumentCaptor.getValue();

        assertThat(newsToSave.getAuthorId()).isEqualTo(requestDTO.getAuthorId());
        assertThat(newsToSave.getTitle()).isEqualTo(requestDTO.getTitle());
        assertThat(newsToSave.getContent()).isEqualTo(requestDTO.getContent());
        assertThat(newsToSave.getCreateDate()).isBetween(createdAtFrom, createdAtTo);

        requestDTO.setTitle("123456789012345678901234567890");
        requestDTO.setContent(
                "1234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890" +
                        "1234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890" +
                        "1234567890123456789012345678901234567890123456789012345"
        );

        assertThatNoException().isThrownBy(() -> newsService.add(requestDTO));
    }

    @Test
    @DisplayName("If a news title is too short, a DTOValidationException will be thrown when calling the add method.")
    void add_titleTooShort_throwsDTOValidateException() throws KeyNullReferenceException {
        EditNewsRequestDTO requestDTO = new EditNewsRequestDTO(
                "12",
                "News content",
                2L
        );
        Mockito.when(authorRepository.existsById(requestDTO.getAuthorId())).thenReturn(true);
        assertThatThrownBy(() -> newsService.add(requestDTO)).isInstanceOf(DTOValidationException.class);
    }

    @Test
    @DisplayName("If a news title is empty, a DTOValidationException will be thrown when calling the add method.")
    void add_emptyTitle_throwsDTOValidateException() throws KeyNullReferenceException {
        EditNewsRequestDTO requestDTO = new EditNewsRequestDTO(
                "",
                "News content",
                12L
        );
        Mockito.when(authorRepository.existsById(requestDTO.getAuthorId())).thenReturn(true);
        assertThatThrownBy(() -> newsService.add(requestDTO)).isInstanceOf(DTOValidationException.class);
    }

    @Test
    @DisplayName("If the news title is too long, a DTOValidationException will be thrown when calling the add method.")
    void add_titleTooLong_throwsDTOValidateException() throws KeyNullReferenceException {
        EditNewsRequestDTO requestDTO = new EditNewsRequestDTO(
                "1234567890123456789012345678901", // 31
                "News content",
                2L
        );
        Mockito.when(authorRepository.existsById(requestDTO.getAuthorId())).thenReturn(true);
        assertThatThrownBy(() -> newsService.add(requestDTO)).isInstanceOf(DTOValidationException.class);
    }

    @Test
    @DisplayName("If the news content is too long, a DTOValidationException will be thrown when calling the add method.")
    void update_contentTooLong_throwsDTOValidateException() throws KeyNullReferenceException {
        EditNewsRequestDTO requestDTO = new EditNewsRequestDTO(
                "1234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890"+ // 100 per line
                        "1234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890"+
                        "1234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890",
                "News content",
                2L
        );
        Mockito.when(authorRepository.existsById(requestDTO.getAuthorId())).thenReturn(true);
        assertThatThrownBy(() -> newsService.add(requestDTO)).isInstanceOf(DTOValidationException.class);
    }

    @Test
    @DisplayName("If a news content is too short, a DTOValidationException will be thrown when calling the add method.")
    void update_contentTooShort_throwsDTOValidateException() throws KeyNullReferenceException {
        EditNewsRequestDTO requestDTO = new EditNewsRequestDTO(
                "News title",
                "123",
                2L
        );
        Mockito.when(authorRepository.existsById(requestDTO.getAuthorId())).thenReturn(true);
        assertThatThrownBy(() -> newsService.add(requestDTO)).isInstanceOf(DTOValidationException.class);
    }

    @Test
    @DisplayName("When passing an incorrect id for author, an AuthorNotFoundException will be thrown")
    void add_notFoundNewAuthor_throwsDTOValidateException() throws KeyNullReferenceException {
        EditNewsRequestDTO requestDTO = new EditNewsRequestDTO(
                "News title",
                "News content",
                2L
        );
        Mockito.when(authorRepository.existsById(requestDTO.getAuthorId())).thenReturn(false);
        assertThatThrownBy(() -> newsService.add(requestDTO)).isInstanceOf(AuthorNotFoundException.class);
    }
}
