package com.mjc.school.datasource;

import com.mjc.school.exception.EntityNotFoundException;
import com.mjc.school.model.News;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class NewsDataSourceTest {
    private static final DataSource<News> dataSource = DataSourceFactory.getDataSource(News.class);
    private static final List<News> readItems = dataSource.findAll();

    private Long findRandomId() {
        Random random = new Random(System.currentTimeMillis());
        return dataSource.findAll().stream()
                .sorted((item1, item2) -> random.nextInt(3) - 1)
                .findAny()
                .map(News::getId)
                .orElse(null);
    }

    @Test
    @DisplayName("All news data has been read")
    void readDataFromFile_allDataHasBeenLoaded_firstNewsDataIsEqualsToExpected() {
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
                2L
                );

        Optional<News> actualNews = readItems.stream().filter(item -> item.getId() == 1L).findFirst();

        assertThat(actualNews).isPresent().contains(expectedNews);
    }

    @Test
    @DisplayName("When an entity is deleted, it is deleted from the dataSource")
    void delete_checkNotExistsInDataSource() {
        Long idToDelete = findRandomId();

        dataSource.findById(idToDelete);
        long expectedCount = dataSource.count() - 1;

        assertThat(dataSource.delete(idToDelete)).isTrue();
        assertThat(dataSource.count()).isEqualTo(expectedCount);

        assertThatThrownBy(() -> dataSource.delete(idToDelete)).isInstanceOf(EntityNotFoundException.class);
        assertThatThrownBy(() -> dataSource.findById(idToDelete)).isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    @DisplayName("When an entity is added, it is saved in the dataset")
    void add_checkExistsInDataSource() {
        News expectedEntity = new News();

        expectedEntity.setTitle("New title");
        expectedEntity.setContent("New content");
        expectedEntity.setCreateDate(LocalDateTime.now());
        expectedEntity.setLastUpdateDate(expectedEntity.getCreateDate());
        expectedEntity.setAuthorId(1L);

        long expectedItemCount = dataSource.count() + 1;

        News addedEntity = dataSource.save(expectedEntity);

        assertThat(dataSource.count()).isEqualTo(expectedItemCount);
        assertThat(addedEntity.getId()).isNotZero();

        expectedEntity.setId(addedEntity.getId());

        News fetchedEntity = dataSource.findById(addedEntity.getId());
        assertThat(fetchedEntity).isNotSameAs(expectedEntity).isEqualTo(expectedEntity);
    }

    @Test
    @DisplayName("When the data is changed, the changes are saved in the dataset")
    void save_update_savedEntityIsEqualsToFetchedEntity_true() {
        long idToChange = findRandomId();
        News expectedEntity = dataSource.findById(idToChange);
        expectedEntity.setTitle("Changed title");
        expectedEntity.setContent("Changed content");
        expectedEntity.setAuthorId(999L);

        long expectedCount = dataSource.count();

        News savedEntity = dataSource.save(expectedEntity);
        News actualEntity = dataSource.findById(savedEntity.getId());

        long actualCount = dataSource.count();

        assertThat(actualCount).isEqualTo(expectedCount);
        assertThat(actualEntity)
                .isNotSameAs(savedEntity)
                .isEqualTo(savedEntity)

                .isNotSameAs(expectedEntity)
                .isEqualTo(expectedEntity);
    }

    @Test
    @DisplayName("When changing the data of an entity issued by a dataSource, the entity data in the dataSource does not change")
    void save_update_OnSavingAndFetchingNewInstanceOfEntityCreatedNotDependOfEachOther_true() {
        Long idForCheck = findRandomId();

        News firstFetchEntity = dataSource.findById(idForCheck);
        News secondFetchEntity_ForChange = dataSource.findById(idForCheck);
        secondFetchEntity_ForChange.setTitle("Change title");
        News actualEntity = dataSource.findById(idForCheck);

        assertThat(actualEntity)
                .isNotSameAs(firstFetchEntity)
                .isEqualTo(firstFetchEntity)

                .isNotSameAs(secondFetchEntity_ForChange)
                .isNotEqualTo(secondFetchEntity_ForChange);
    }

    @Test
    @DisplayName("When trying to search by an existing id, an entity with the corresponding id will be returned")
    void findById_found() {
        Long idForFetch = findRandomId();
        News news = dataSource.findById(idForFetch);
        assertThat(news).isNotNull().extracting("id").isEqualTo(news.getId());
    }

    @Test
    @DisplayName("If you try to search on a non-existent ID, an exception EntityNotFoundException is thrown")
    void findById_notFound_nullAsResult() {
        assertThatThrownBy(() -> dataSource.findById(-1L)).isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    @DisplayName("findAll with assigned limit parameter returns a list of items with a size equal to the limit value")
    void testLimit() {
        int offset = 0;
        int limit  = 1;

        List<News> readItems = dataSource.findAll(offset, limit);

        assertThat(readItems).hasSize(limit);

        limit  = 2;

        readItems = dataSource.findAll(offset, limit);

        assertThat(readItems).hasSize(limit);
    }

    @Test
    @DisplayName("When offset is equals to 1 findAll returns only one item in result list")
    void test() {
        List<News> allItems = dataSource.findAll();

        int offset = 1;
        int limit  = 1;
        List<News> actualItems = dataSource.findAll(offset, limit);
        List<News> expectedItems = Collections.singletonList(allItems.get(1));
        assertThat(actualItems).containsExactlyElementsOf(expectedItems);

        expectedItems = new ArrayList<>(allItems);
        expectedItems.remove(0);
        limit = -1;
        actualItems = dataSource.findAll(offset, limit);

        assertThat(actualItems).containsExactlyElementsOf(expectedItems);
    }
}
