package com.mjc.school.datasource;

import com.mjc.school.exception.UnsupportedModelException;
import com.mjc.school.model.Author;
import com.mjc.school.model.Model;
import com.mjc.school.model.News;

public class DataSourceFactory {

    private DataSourceFactory() {}

    public static <T extends Model> DataSource<T> getDataSource(Class<T> modelClass) {
        if (modelClass == Author.class) {
            return (DataSource<T>) AuthorDataSource.getInstance();
        } else if (modelClass == News.class) {
            return (DataSource<T>) NewsDataSource.getInstance();
        }

        throw new UnsupportedModelException(modelClass.getCanonicalName());
    }
}
