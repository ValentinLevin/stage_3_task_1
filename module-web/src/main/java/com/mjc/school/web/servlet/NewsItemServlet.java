package com.mjc.school.web.servlet;

import com.mjc.school.service.dto.EditNewsRequestDTO;
import com.mjc.school.service.dto.NewsDTO;
import com.mjc.school.service.exception.*;
import com.mjc.school.service.service.NewsService;
import com.mjc.school.web.dto.BaseResponseDTO;
import com.mjc.school.web.dto.GetNewsItemResponseDTO;
import com.mjc.school.web.dto.UpdateNewsResponseDTO;
import com.mjc.school.web.exception.*;
import com.mjc.school.web.util.HttpServletRequestUtils;
import com.mjc.school.web.util.HttpServletResponseUtils;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import static jakarta.servlet.http.HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
import static jakarta.servlet.http.HttpServletResponse.SC_OK;

@WebServlet("/news/*")
@Slf4j
public class NewsItemServlet extends HttpServlet {
    private final NewsService newsService;

    public NewsItemServlet(NewsService newsService) {
        this.newsService = newsService;
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) {
        long newsId = 0;
        try {
            newsId = HttpServletRequestUtils.getIdFromPath(req);

            EditNewsRequestDTO newsDTO = HttpServletRequestUtils.readObjectFromRequestBody(req, EditNewsRequestDTO.class);
            NewsDTO editedNewsDTO;
            try {
                editedNewsDTO = newsService.update(newsId, newsDTO);
            } catch (AuthorNotFoundException e) {
                throw new AuthorNotFoundWebException(newsDTO.getAuthorId());
            } catch (NullNewsIdException e) {
                throw new IllegalNewsIdValueWebException("null");
            } catch (NullAuthorIdException e) {
                throw new IllegalAuthorIdValueWebException("null");
            } catch (DTOValidationException e) {
                throw new DataValidationWebException(e.getMessage());
            } catch (NewsNotFoundException e) {
                throw new NewsNotFoundWebException(newsId);
            }

            UpdateNewsResponseDTO responseBody = new UpdateNewsResponseDTO(editedNewsDTO);
            HttpServletResponseUtils.writePayloadIntoResponseBody(resp, responseBody, SC_OK);
        } catch (CustomWebException e) {
            BaseResponseDTO responseBody = new BaseResponseDTO(e);
            HttpServletResponseUtils.writePayloadIntoResponseBody(resp, responseBody, e.getHttpStatus());
        } catch (CustomWebRuntimeException e) {
            BaseResponseDTO responseBody = new BaseResponseDTO(e);
            HttpServletResponseUtils.writePayloadIntoResponseBody(resp, responseBody, e.getHttpStatus());
        } catch (RuntimeException e) {
            log.error("Error when processing a request to change news data with id {}", newsId, e);
            BaseResponseDTO responseBody = new BaseResponseDTO(e);
            HttpServletResponseUtils.writePayloadIntoResponseBody(resp, responseBody, SC_INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        long newsId = 0;
        try {
            newsId = HttpServletRequestUtils.getIdFromPath(req);

            NewsDTO newsDTO;
            try {
                newsDTO = newsService.findById(newsId);
            } catch (NullAuthorIdException | AuthorNotFoundException e) {
                log.error("Unexpected error when requesting news by id {}", newsId, e);
                throw new IllegalNewsIdValueWebException("null");
            } catch (NullNewsIdException e) {
                throw new IllegalNewsIdValueWebException("null");
            } catch (NewsNotFoundException e) {
                throw new NewsNotFoundWebException(newsId);
            }
            GetNewsItemResponseDTO responseBody = new GetNewsItemResponseDTO(newsDTO);
            HttpServletResponseUtils.writePayloadIntoResponseBody(resp, responseBody, SC_OK);
        } catch (CustomWebException e) {
            BaseResponseDTO responseBody = new BaseResponseDTO(e);
            HttpServletResponseUtils.writePayloadIntoResponseBody(resp, responseBody, e.getHttpStatus());
        } catch (RuntimeException e) {
            log.error("Error when requesting news by id {}", newsId, e);
            BaseResponseDTO responseBody = new BaseResponseDTO(e);
            HttpServletResponseUtils.writePayloadIntoResponseBody(resp, responseBody, SC_INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) {
        long newsId = 0;
        try {
            newsId = HttpServletRequestUtils.getIdFromPath(req);
            try {
                newsService.deleteById(newsId);
            } catch (NullNewsIdException e) {
                throw new IllegalNewsIdValueWebException("null");
            } catch (NewsNotFoundException e) {
                throw new NewsNotFoundWebException(newsId);
            }
            HttpServletResponseUtils.writePayloadIntoResponseBody(resp, new BaseResponseDTO(), SC_OK);
        } catch (CustomWebException e) {
            BaseResponseDTO responseBody = new BaseResponseDTO(e);
            HttpServletResponseUtils.writePayloadIntoResponseBody(resp, responseBody, e.getHttpStatus());
        } catch (RuntimeException e) {
            log.error("Error when deleting news by id {}", newsId, e);
            BaseResponseDTO responseBody = new BaseResponseDTO(e);
            HttpServletResponseUtils.writePayloadIntoResponseBody(resp, responseBody, SC_INTERNAL_SERVER_ERROR);
        }
    }
}
