package com.mjc.school.repository;

import com.mjc.school.datasource.DataSource;
import com.mjc.school.model.Author;

class AuthorRepository extends RepositoryImpl<Author> implements Repository<Author> {
    public AuthorRepository(DataSource<Author> dataSource) {
        super(dataSource);
    }
}
