package com.mjc.school.repository;

import com.mjc.school.datasource.DataSource;
import com.mjc.school.model.Author;

public class AuthorRepository extends RepositoryImpl<Author> implements Repository<Author> {
    private static AuthorRepository instance = null;

    private AuthorRepository(DataSource<Author> dataSource) {
        super(dataSource);
    }

    public static AuthorRepository getInstance(DataSource<Author> dataSource) {
        if (instance == null) {
            synchronized (AuthorRepository.class) {
                if (instance == null) {
                    instance = new AuthorRepository(dataSource);
                }
            }
        }

        return instance;
    }
}
