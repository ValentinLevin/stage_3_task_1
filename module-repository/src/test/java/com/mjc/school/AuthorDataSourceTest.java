package com.mjc.school;

import com.mjc.school.datasource.DataSource;
import com.mjc.school.datasource.DataSourceFactory;
import com.mjc.school.model.Author;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class AuthorDataSourceTest {
    @Test
    @DisplayName("Проверка загрузки данных из файла по авторам")
    void dataFromFileCheck() {
        DataSource<Author> dataSource = DataSourceFactory.getDataSource(Author.class);
        long expectedValue = 3;
        Assertions.assertEquals(expectedValue, dataSource.count());
    }
}
