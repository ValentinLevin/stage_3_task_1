package com.mjc.school.service;

import com.mjc.school.dto.EditNewsRequestDTO;
import com.mjc.school.dto.NewsDTO;

import java.util.List;

public interface NewsService {
    NewsDTO add(EditNewsRequestDTO newsDTO);
    NewsDTO update(Long newsId, EditNewsRequestDTO newsDTO);
    NewsDTO findById(long id);
    List<NewsDTO> findAll();
    boolean deleteById(long id);
}
