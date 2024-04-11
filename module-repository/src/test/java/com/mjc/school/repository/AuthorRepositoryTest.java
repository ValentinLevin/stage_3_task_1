package com.mjc.school.repository;

import com.mjc.school.datasource.DataSource;
import com.mjc.school.exception.EntityNullReferenceException;
import com.mjc.school.exception.KeyNullReferenceException;
import com.mjc.school.model.Author;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class AuthorRepositoryTest {
    private DataSource<Author> dataSource;
    private Repository<Author> repository;

    @BeforeEach
    void setup() {
    }

    @Test
    @DisplayName("При запросе автора по id был вызван соответствующий метов dataSource")
    void findById() {
        dataSource = Mockito.mock(DataSource.class);
        repository = AuthorRepository.getInstance(dataSource);

        Author expectedAuthor = new Author(1L, "Author 1 name");
        Mockito.when(dataSource.findById(1L)).thenReturn(expectedAuthor);

        Author actualAuthor = this.repository.findById(1L);

        assertThat(actualAuthor).isEqualTo(expectedAuthor);
//        Mockito.verify(dataSource, Mockito.only()).findById(1L);
//        Mockito.verify(dataSource, Mockito.times(1)).findById(1L);
    }

    @Test
    @DisplayName("При попытке сохранения Null объекта будет проброшено исключение EntityNullReferenceException")
    void save_entityNullReference_throwsEntityNullReferenceException() {
        assertThatThrownBy(() -> repository.save(null)).isInstanceOf(EntityNullReferenceException.class);
    }

    @Test
    @DisplayName("При попытке запроса объекта по Null ключу будет проброшено исключение EntityNullReferenceException")
    void findById_keyNullReference_throwsKeyNullReferenceException() {
        assertThatThrownBy(() -> repository.findById(null)).isInstanceOf(KeyNullReferenceException.class);
    }
}
