package com.mjc.school.repository;

import com.mjc.school.datasource.DataSource;
import com.mjc.school.datasource.DataSourceFactory;
import com.mjc.school.exception.UnsupportedEntityClassException;
import com.mjc.school.model.Author;
import com.mjc.school.model.Entity;
import com.mjc.school.model.News;

public class RepositoryFactory {

    public static <T extends Entity> Repository<T> getRepository(Class<T> entityClass) {
        if (entityClass == Author.class) {
            DataSource<Author> dataSource = DataSourceFactory.getDataSource(Author.class);
            return (Repository<T>) AuthorRepository.getInstance(dataSource);
        } else if (entityClass == News.class) {
            DataSource<News> dataSource = DataSourceFactory.getDataSource(News.class);
            return (Repository<T>) NewsRepository.getInstance(dataSource);
        }

        throw new UnsupportedEntityClassException(entityClass);
    }
}
