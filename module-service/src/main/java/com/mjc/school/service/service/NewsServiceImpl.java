package com.mjc.school.service.service;

import com.mjc.school.repository.exception.EntityNotFoundException;
import com.mjc.school.repository.exception.EntityNullReferenceException;
import com.mjc.school.repository.exception.EntityValidationException;
import com.mjc.school.repository.exception.KeyNullReferenceException;
import com.mjc.school.repository.model.Author;
import com.mjc.school.repository.model.News;
import com.mjc.school.repository.repository.Repository;
import com.mjc.school.service.dto.EditNewsRequestDTO;
import com.mjc.school.service.dto.NewsDTO;
import com.mjc.school.service.exception.*;
import com.mjc.school.service.mapper.AuthorMapper;
import com.mjc.school.service.mapper.NewsMapper;
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
    public NewsDTO add(
            EditNewsRequestDTO newsDTO
    ) throws DTOValidationException, AuthorNotFoundException, NullAuthorIdException, NewsNotFoundException, NullNewsIdException {
        validateDTO(newsDTO);

        try {
            if (!authorRepository.existsById(newsDTO.getAuthorId())) {
                throw new AuthorNotFoundException(newsDTO.getAuthorId());
            }
        } catch (KeyNullReferenceException e) {
            throw new NullAuthorIdException();
        }

        News news = NewsMapper.fromEditNewsRequestDTO(newsDTO);
        news.setCreateDate(LocalDateTime.now());
        news.setLastUpdateDate(news.getCreateDate());

        try {
            news = newsRepository.save(news);
        } catch (EntityValidationException | EntityNullReferenceException e) {
            throw new DTOValidationException(e.getMessage());
        }

        return findById(news.getId());
    }

    @Override
    public NewsDTO update(
            Long newsId, EditNewsRequestDTO newsDTO
    ) throws DTOValidationException, NullNewsIdException, NewsNotFoundException, AuthorNotFoundException, NullAuthorIdException {
        if (newsId == null || newsId <= 0) {
            throw new DTOValidationException(String.format("Incorrect news id value %d", newsId));
        }

        validateDTO(newsDTO);

        News news;
        try {
            news = newsRepository.findById(newsId);
        } catch (KeyNullReferenceException e) {
            throw new NullNewsIdException();
        } catch (EntityNotFoundException e) {
            throw new NewsNotFoundException(newsId);
        }

        try {
            if (!authorRepository.existsById(newsDTO.getAuthorId())) {
                throw new AuthorNotFoundException(newsDTO.getAuthorId());
            }
        } catch (KeyNullReferenceException e) {
            throw new NullAuthorIdException();
        }

        news.setTitle(newsDTO.getTitle());
        news.setContent(newsDTO.getContent());
        news.setAuthorId(newsDTO.getAuthorId());
        news.setLastUpdateDate(LocalDateTime.now());

        try {
            news = newsRepository.save(news);
        } catch (EntityNullReferenceException e) {
            throw new NullNewsIdException();
        } catch (EntityValidationException e) {
            throw new DTOValidationException(e.getMessage());
        }

        return findById(news.getId());
    }

    @Override
    public NewsDTO findById(long id) throws NullNewsIdException, NewsNotFoundException, NullAuthorIdException, AuthorNotFoundException {
        News news;
        try {
            news = this.newsRepository.findById(id);
        } catch (KeyNullReferenceException e) {
            throw new NullNewsIdException();
        } catch (EntityNotFoundException e) {
            throw new NewsNotFoundException(id);
        }

        Author author;
        try {
            author = this.authorRepository.findById(news.getAuthorId());
        } catch (KeyNullReferenceException e) {
            throw new NullAuthorIdException();
        } catch (EntityNotFoundException e) {
            throw new AuthorNotFoundException(news.getAuthorId());
        }

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
    public boolean deleteById(long id) throws NullNewsIdException, NewsNotFoundException {
        try {
            return this.newsRepository.deleteById(id);
        } catch (KeyNullReferenceException e) {
            throw new NullNewsIdException();
        } catch (EntityNotFoundException e) {
            throw new NewsNotFoundException(id);
        }
    }

    @Override
    public long count() {
        return this.newsRepository.count();
    }

    private <T> void validateDTO(T object) throws DTOValidationException {
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
