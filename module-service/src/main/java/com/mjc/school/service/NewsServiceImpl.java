package com.mjc.school.service;

import com.mjc.school.dto.EditNewsRequestDTO;
import com.mjc.school.dto.NewsDTO;
import com.mjc.school.exception.AuthorNotFoundException;
import com.mjc.school.exception.DTOValidationException;
import com.mjc.school.exception.EntityNotFoundException;
import com.mjc.school.exception.NewsNotFoundException;
import com.mjc.school.mapper.AuthorMapper;
import com.mjc.school.mapper.NewsMapper;
import com.mjc.school.model.Author;
import com.mjc.school.model.News;
import com.mjc.school.repository.Repository;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

class NewsServiceImpl implements NewsService {
    private final Repository<News> newsRepository;
    private final Repository<Author> authorRepository;
    private static final Validator validator;

    static {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            validator = factory.getValidator();
        }
    }

    public NewsServiceImpl(
            Repository<News> newsRepository,
            Repository<Author> authorRepository
    ) {
        this.newsRepository = newsRepository;
        this.authorRepository = authorRepository;
    }

    @Override
    public NewsDTO add(EditNewsRequestDTO newsDTO) {
        validateDTO(newsDTO);

        if (!authorRepository.existsById(newsDTO.getAuthorId())) {
            throw new AuthorNotFoundException(newsDTO.getAuthorId());
        }

        News news = NewsMapper.fromEditNewsRequestDTO(newsDTO);
        news.setCreateDate(LocalDateTime.now());
        news.setLastUpdateDate(news.getCreateDate());

        news = newsRepository.save(news);

        return findById(news.getId());
    }

    @Override
    public NewsDTO update(Long newsId, EditNewsRequestDTO newsDTO) {
        if (newsId == null || newsId <= 0) {
            throw new DTOValidationException(String.format("Incorrect news id value %d", newsId));
        }

        validateDTO(newsDTO);

        News news;
        try {
            news = newsRepository.findById(newsId);
        } catch (EntityNotFoundException e) {
            throw new NewsNotFoundException(newsId);
        }

        if (!authorRepository.existsById(newsDTO.getAuthorId())) {
            throw new AuthorNotFoundException(newsDTO.getAuthorId());
        }

        news.setTitle(newsDTO.getTitle());
        news.setContent(newsDTO.getContent());
        news.setAuthorId(newsDTO.getAuthorId());
        news.setLastUpdateDate(LocalDateTime.now());

        news = newsRepository.save(news);

        return findById(news.getId());
    }

    @Override
    public NewsDTO findById(long id) {
        News news;
        try {
            news = this.newsRepository.findById(id);
        } catch (EntityNotFoundException e) {
            throw new NewsNotFoundException(id);
        }

        Author author = this.authorRepository.findById(news.getAuthorId());

        NewsDTO newsDTO = NewsMapper.toNewsDTO(news);
        newsDTO.setAuthor(author != null ? AuthorMapper.toAuthorDTO(author) : null);

        return newsDTO;
    }

    @Override
    public List<NewsDTO> findAll() {
        return this.findAll(0, -1);
    }

    @Override
    public List<NewsDTO> findAll(long offset, long limit) {
        Map<Long, Author> authors =
                this.authorRepository.findAll().stream()
                        .collect(Collectors.toMap(Author::getId, item -> item));

        List<News> news;
        if (offset == 0 && limit == -1) {
            news = this.newsRepository.findAll();
        } else {
            news = this.newsRepository.findAll(offset, limit);
        }

        return news.stream()
                .map(item -> {
                    NewsDTO newsDTO = NewsMapper.toNewsDTO(item);
                    newsDTO.setAuthor(AuthorMapper.toAuthorDTO(authors.get(item.getAuthorId())));
                    return newsDTO;
                })
                .toList();
    }

    @Override
    public boolean deleteById(long id) {
        try {
            return this.newsRepository.deleteById(id);
        } catch (EntityNotFoundException e) {
            throw new NewsNotFoundException(id);
        }
    }

    @Override
    public long count() {
        return this.newsRepository.count();
    }

    private <T> void validateDTO(T object) {
        if (object == null) {
            throw new DTOValidationException("Passed a null object as the object to add");
        }

        Set<ConstraintViolation<T>> constraintViolations = validator.validate(object);
        if (!constraintViolations.isEmpty()) {
            throw new DTOValidationException(
                    constraintViolations.stream()
                            .map( cv -> cv == null ? "null" : cv.getPropertyPath() + ": " + cv.getMessage() )
                            .collect( Collectors.joining( ", " ) )
            );
        }
    }
}
