package com.mjc.school.datasource;

import com.mjc.school.model.News;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;

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
                1L
                );

        Optional<News> actualNews = readItems.stream().filter(item -> item.getId() == 1L).findFirst();

        assertThat(actualNews).isPresent().contains(expectedNews);
    }

    @Test
    @DisplayName("When an entity is deleted, it is deleted from the dataSource")
    void delete_checkNotExistsInDataSource() {
        Long idToDelete = findRandomId();

        News itemToDelete = dataSource.findById(idToDelete);
        long expectedCount = dataSource.count() - 1;

        News removedItem = dataSource.delete(idToDelete);
        News actualRecordAfterDelete = dataSource.findById(idToDelete);

        assertThat(removedItem).isEqualTo(itemToDelete);
        assertThat(actualRecordAfterDelete).isNull();
        assertThat(dataSource.count()).isEqualTo(expectedCount);
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
        assertThat(addedEntity).isNotSameAs(expectedEntity).isEqualTo(expectedEntity);

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
}
