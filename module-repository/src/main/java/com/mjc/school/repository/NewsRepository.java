package com.mjc.school.repository;

import com.mjc.school.datasource.DataSource;
import com.mjc.school.model.Author;
import com.mjc.school.model.News;

public class NewsRepository extends RepositoryImpl<News> implements Repository<News> {
    private static NewsRepository instance = null;

    private NewsRepository(DataSource<News> dataSource) {
        super(dataSource);
    }

    public static NewsRepository getInstance(DataSource<News> dataSource) {
        if (instance == null) {
            synchronized (NewsRepository.class) {
                if (instance == null) {
                    instance = new NewsRepository(dataSource);
                }
            }
        }

        return instance;
    }
}
