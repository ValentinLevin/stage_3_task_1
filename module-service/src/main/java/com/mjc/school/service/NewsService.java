package com.mjc.school.service;

import com.mjc.school.dto.EditNewsRequestDTO;
import com.mjc.school.dto.NewsDTO;

import java.io.Serializable;
import java.util.List;

public interface NewsService {
    NewsDTO add(EditNewsRequestDTO newsDTO);
    NewsDTO update(Long newsId, EditNewsRequestDTO newsDTO);
    NewsDTO findById(long id);
    List<NewsDTO> findAll();

    /**
     * @param offset number of news to skip. If the value is zero, the news items will be taken from the first one
     * @param limit number of news no more than the method should return. If the value of "limit" parameter is -1, all the news items of the dataset will be returned
     * @return list of news
     */
    List<NewsDTO> findAll(long offset, long limit);
    boolean deleteById(long id);
    long count();
}
