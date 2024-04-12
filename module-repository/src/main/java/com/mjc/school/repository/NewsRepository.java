package com.mjc.school.repository;

import com.mjc.school.datasource.DataSource;
import com.mjc.school.model.Author;
import com.mjc.school.model.News;

public class NewsRepository extends RepositoryImpl<News> implements Repository<News> {
    public NewsRepository(DataSource<News> dataSource) {
        super(dataSource);
    }
}
