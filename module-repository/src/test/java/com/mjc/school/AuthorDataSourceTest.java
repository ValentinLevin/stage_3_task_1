package com.mjc.school;

import com.mjc.school.datasource.DataSource;
import com.mjc.school.datasource.DataSourceFactory;
import com.mjc.school.model.Author;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class AuthorDataSourceTest {
    private DataSource<Author> authorDataSource;

    @BeforeEach
    void loadDataSource() {
        this.authorDataSource = DataSourceFactory.getDataSource(Author.class);
    }

    @Test
    @DisplayName("Данные 1-го автора считаны корректно")
    void firstAuthorDataTest() {
        Author expectedAuthorData = new Author(1L, "Author 1 name");
        Author actualAuthorData = authorDataSource.findById(1L);
        assertThat(actualAuthorData).isEqualTo(expectedAuthorData);
    }

    @Test
    @DisplayName("При удалении одной сущности была удалена только одна сущность")
    void afterRemoveAuthorTestCount() {
        List<Author> authors = authorDataSource.findAll();
        long expectedAuthorCountAfterRemove = authorDataSource.count()-1;

        Long idToRemove = authors.get(0).getId();
        authorDataSource.remove(idToRemove);

        assertThat(authorDataSource.count()).isEqualTo(expectedAuthorCountAfterRemove);
        assertThat(authorDataSource.findById(idToRemove)).isNull();
    }

    @Test
    @DisplayName("При удалении сущности была удалена именно выбранная сущность")
    void removedAuthorTest() {
        List<Author> authors = authorDataSource.findAll();
        long idToRemove = authors.get(0).getId();
        Author removedAuthor = authorDataSource.remove(idToRemove);
        assertThat(authorDataSource.findById(idToRemove)).isNull();
        assertThat(removedAuthor.getId()).isEqualTo(idToRemove);
    }

    @Test
    @DisplayName("После добавления новой сущности количество записей увеличилось на 1")
    void checkAuthorCountAfterAddingNewAuthor() {
        long expectedAuthorCount = authorDataSource.count()+1;

        Author newAuthor = new Author("New Author");
        authorDataSource.save(newAuthor);

        assertThat(authorDataSource.count()).isEqualTo(expectedAuthorCount);
    }

    @Test
    @DisplayName("После добавления новой сущности данные автора полученные из dataSource соответствуют добавленным данным")
    void checkAddedAuthorAfterAddingNewAuthor() {
        Author newAuthor = new Author("New Author");
        Author addedAuthor = authorDataSource.save(newAuthor);
        Author newAuthorDataReceivedFromDataSource = authorDataSource.findById(addedAuthor.getId());

        assertThat(newAuthor.getName()).isEqualTo(newAuthorDataReceivedFromDataSource.getName());
    }

    @Test
    @DisplayName("При изменении данных сущности, выданной dataSource, не изменяются данные сущности в dataSource")
    void checkImmutable() {
        List<Author> authors = authorDataSource.findAll();
        Long idForCheck = authors.get(0).getId();

        Author expectedAuthor = authorDataSource.findById(idForCheck);

        Author authorForChange = authorDataSource.findById(idForCheck);

        authorForChange.setName("Change author name");

        Author actualAuthor = authorDataSource.findById(idForCheck);

        assertThat(actualAuthor)
                .isNotSameAs(expectedAuthor)
                .isEqualTo(expectedAuthor)
                .isNotEqualTo(authorForChange);
    }

    @Test
    @DisplayName("После сохранения изменений сущности в dataSource, при повторном запросе данные возвращаются измененными")
    void checkUpdate() {
        List<Author> authors = this.authorDataSource.findAll();
        Author expectedAuthor = this.authorDataSource.findById(authors.get(0).getId());

        expectedAuthor.setName("Changed author name");

        authorDataSource.save(expectedAuthor);

        Author actualAuthor = authorDataSource.findById(expectedAuthor.getId());

        assertThat(actualAuthor)
                .isNotSameAs(expectedAuthor)
                .isEqualTo(expectedAuthor);
    }
}
