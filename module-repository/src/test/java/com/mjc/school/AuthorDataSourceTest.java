package com.mjc.school;

import com.mjc.school.datasource.DataSource;
import com.mjc.school.datasource.DataSourceFactory;
import com.mjc.school.model.Author;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.tuple;


class AuthorDataSourceTest {
    private static final DataSource<Author> dataSource = DataSourceFactory.getDataSource(Author.class);
    private static final List<Author> readItems = dataSource.findAll();

    @Test
    @DisplayName("Данные из файла считаны корректно")
    void firstAuthorDataTest() {
        Author expectedAuthorData = new Author(1L, "Author 1 name");
        Optional<Author> actualAuthorData = readItems.stream().filter(item -> item.getId() == 1L).findFirst();
        assertThat(actualAuthorData).isPresent().contains(expectedAuthorData);
        assertThat(readItems)
                .extracting("id", "name")
                .containsOnly(tuple(1L, "Author 1 name"), tuple(2L, "Author 2 name"), tuple(3L, "Author 3 name"));
    }

    @Test
    @DisplayName("При удалении одной сущности была удалена только одна сущность")
    void afterRemoveAuthorTestCount() {
        List<Author> authors = dataSource.findAll();
        long expectedAuthorCountAfterRemove = dataSource.count()-1;

        Long idToRemove = authors.get(0).getId();
        dataSource.remove(idToRemove);

        assertThat(dataSource.count()).isEqualTo(expectedAuthorCountAfterRemove);
        assertThat(dataSource.findById(idToRemove)).isNull();
    }

    @Test
    @DisplayName("При удалении сущности была удалена именно выбранная сущность")
    void removedAuthorTest() {
        List<Author> authors = dataSource.findAll();
        long idToRemove = authors.get(0).getId();
        Author removedAuthor = dataSource.remove(idToRemove);
        assertThat(dataSource.findById(idToRemove)).isNull();
        assertThat(removedAuthor.getId()).isEqualTo(idToRemove);
    }

    @Test
    @DisplayName("После добавления новой сущности количество записей увеличилось на 1")
    void checkAuthorCountAfterAddingNewAuthor() {
        long expectedAuthorCount = dataSource.count()+1;

        Author newAuthor = new Author("New Author");
        dataSource.save(newAuthor);

        assertThat(dataSource.count()).isEqualTo(expectedAuthorCount);
    }

    @Test
    @DisplayName("После добавления новой сущности данные автора, полученные из dataSource соответствуют добавленным данным")
    void checkAddedAuthorAfterAddingNewAuthor() {
        Author newAuthor = new Author("New Author");
        Author addedAuthor = dataSource.save(newAuthor);
        Author newAuthorDataReceivedFromDataSource = dataSource.findById(addedAuthor.getId());

        assertThat(newAuthor.getName()).isEqualTo(newAuthorDataReceivedFromDataSource.getName());
    }

    @Test
    @DisplayName("При изменении данных сущности, выданной dataSource, не изменяются данные сущности в dataSource")
    void checkImmutable() {
        List<Author> authors = dataSource.findAll();
        Long idForCheck = authors.get(0).getId();

        Author expectedAuthor = dataSource.findById(idForCheck);

        Author authorForChange = dataSource.findById(idForCheck);

        authorForChange.setName("Change author name");

        Author actualAuthor = dataSource.findById(idForCheck);

        assertThat(actualAuthor)
                .isNotSameAs(expectedAuthor)
                .isEqualTo(expectedAuthor)
                .isNotEqualTo(authorForChange);
    }

    @Test
    @DisplayName("После сохранения изменений сущности в dataSource, при повторном запросе данные возвращаются измененными")
    void checkUpdate() {
        List<Author> authors = dataSource.findAll();
        Author expectedAuthor = dataSource.findById(authors.get(0).getId());

        expectedAuthor.setName("Changed author name");

        dataSource.save(expectedAuthor);

        Author actualAuthor = dataSource.findById(expectedAuthor.getId());

        assertThat(actualAuthor)
                .isNotSameAs(expectedAuthor)
                .isEqualTo(expectedAuthor);
    }
}
