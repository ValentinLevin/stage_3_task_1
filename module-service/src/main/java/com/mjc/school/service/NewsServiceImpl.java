package com.mjc.school.service;

import com.mjc.school.dto.NewsDTO;
import com.mjc.school.mapper.AuthorDTOMapper;
import com.mjc.school.mapper.NewsDTOMapper;
import com.mjc.school.model.Author;
import com.mjc.school.model.News;
import com.mjc.school.repository.Repository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

class NewsServiceImpl implements NewsService {
    private final Repository<News> newsRepository;
    private final Repository<Author> authorRepository;

    public NewsServiceImpl(
            Repository<News> newsRepository,
            Repository<Author> authorRepository
    ) {
        this.newsRepository = newsRepository;
        this.authorRepository = authorRepository;
    }

    @Override
    public NewsDTO add(NewsDTO entity) {
        return null;
    }

    @Override
    public NewsDTO update(NewsDTO entity) {
        return null;
    }

    @Override
    public NewsDTO findById(long id) {
        News news = this.newsRepository.findById(id);
        if (news != null) {
            Author author = this.authorRepository.findById(news.getAuthorId());

            NewsDTO newsDTO = NewsDTOMapper.fromNews(news);
            newsDTO.setAuthor(author != null ? AuthorDTOMapper.fromAuthor(author) : null);

            return newsDTO;
        } else {
            return null;
        }

    }

    @Override
    public List<NewsDTO> findAll() {
        Map<Long, Author> authors =
                this.authorRepository.findAll().stream()
                        .collect(Collectors.toMap(Author::getId, item -> item));

        List<News> news = this.newsRepository.findAll();
        return news.stream()
                .map(item -> {
                    NewsDTO newsDTO = NewsDTOMapper.fromNews(item);
                    newsDTO.setAuthor(AuthorDTOMapper.fromAuthor(authors.get(item.getId())));
                    return newsDTO;
                })
                .toList();
    }

    @Override
    public boolean deleteById(long id) {
        return this.newsRepository.deleteById(id);
    }
}
