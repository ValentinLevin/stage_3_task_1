package com.mjc.school.service;

import com.mjc.school.dto.NewsDTO;

import java.util.List;

public interface NewsService {
    NewsDTO add(NewsDTO entity);
    NewsDTO update(NewsDTO entity);
    NewsDTO findById(long id);
    List<NewsDTO> findAll();
    boolean  deleteById(long id);
}
