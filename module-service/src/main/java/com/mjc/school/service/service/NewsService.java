package com.mjc.school.service.service;

import com.mjc.school.service.dto.EditNewsRequestDTO;
import com.mjc.school.service.dto.NewsDTO;
import com.mjc.school.service.exception.*;

import java.util.List;

public interface NewsService {
    NewsDTO add(EditNewsRequestDTO newsDTO) throws DTOValidationException, AuthorNotFoundException, NullAuthorIdException, NewsNotFoundException, NullNewsIdException;
    NewsDTO update(Long newsId, EditNewsRequestDTO newsDTO) throws DTOValidationException, NullNewsIdException, NewsNotFoundException, NullAuthorIdException, AuthorNotFoundException;
    NewsDTO findById(long id) throws NullNewsIdException, NewsNotFoundException, NullAuthorIdException, AuthorNotFoundException;
    List<NewsDTO> findAll();

    /**
     * @param offset number of news to skip. If the value is zero, the news items will be taken from the first one
     * @param limit number of news no more than the method should return. If the value of "limit" parameter is -1, all the news items of the dataset will be returned
     * @return list of news
     */
    List<NewsDTO> findAll(long offset, long limit);
    boolean deleteById(long id) throws NullNewsIdException, NewsNotFoundException;
    long count();
}
