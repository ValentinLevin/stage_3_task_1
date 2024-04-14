package com.mjc.school.service;

import com.mjc.school.model.Author;
import com.mjc.school.model.News;
import com.mjc.school.repository.RepositoryFactory;

public class NewsServiceFactory {
    private NewsServiceFactory() {}

    public static NewsService newsService() {
        return new NewsServiceImpl(
                RepositoryFactory.getRepository(News.class),
                RepositoryFactory.getRepository(Author.class)
        );
    }
}
