package com.mjc.school.web.servlet;

import com.mjc.school.service.dto.EditNewsRequestDTO;
import com.mjc.school.service.dto.NewsDTO;
import com.mjc.school.service.exception.*;
import com.mjc.school.service.service.NewsService;
import com.mjc.school.web.dto.AddNewsResponseDTO;
import com.mjc.school.web.dto.BaseResponseDTO;
import com.mjc.school.web.dto.GetNewsListResponseDTO;
import com.mjc.school.web.exception.*;
import com.mjc.school.web.util.HttpServletRequestUtils;
import com.mjc.school.web.util.HttpServletResponseUtils;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import static jakarta.servlet.http.HttpServletResponse.*;

@WebServlet("/news")
@Slf4j
public class NewsServlet extends HttpServlet {
    private final NewsService newsService;

    public NewsServlet(NewsService newsService) {
        this.newsService = newsService;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)  {
        try {
            int limit = HttpServletRequestUtils.getLimitValueFromRequest(req);
            int offset = HttpServletRequestUtils.getOffsetValueFromRequest(req);
            List<NewsDTO> news = this.newsService.findAll(offset, limit);
            long totalCount = this.newsService.count();
            GetNewsListResponseDTO responseBody = new GetNewsListResponseDTO(news, offset+1L, news.size(), totalCount);

            HttpServletResponseUtils.writePayloadIntoResponseBody(resp, responseBody, SC_OK);
        } catch (CustomWebException e) {
            BaseResponseDTO responseBody = new BaseResponseDTO(e);
            HttpServletResponseUtils.writePayloadIntoResponseBody(resp, responseBody, e.getHttpStatus());
        } catch (CustomWebRuntimeException e) {
            BaseResponseDTO responseBody = new BaseResponseDTO(e);
            HttpServletResponseUtils.writePayloadIntoResponseBody(resp, responseBody, e.getHttpStatus());
        } catch (RuntimeException e) {
            log.error("Error when processing a request to get a list of news", e);
            BaseResponseDTO responseBody = new BaseResponseDTO(e);
            HttpServletResponseUtils.writePayloadIntoResponseBody(resp, responseBody, SC_INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        try {
            EditNewsRequestDTO newsDTO = HttpServletRequestUtils.readObjectFromRequestBody(req, EditNewsRequestDTO.class);
            NewsDTO createdNewsDTO;

            try {
                createdNewsDTO = newsService.add(newsDTO);
            } catch (NullAuthorIdException e) {
                throw new IllegalAuthorIdValueWebException("null");
            } catch (DTOValidationException e) {
                throw new DataValidationWebException(e.getMessage());
            } catch (AuthorNotFoundException e) {
                throw new AuthorNotFoundWebException(newsDTO.getAuthorId());
            } catch (NullNewsIdException | NewsNotFoundException e) {
                log.error("An unexpected exception was thrown while adding", e);
                throw new CustomWebRuntimeException();
            }

            AddNewsResponseDTO responseBody = new AddNewsResponseDTO(createdNewsDTO);

            try {
                URI createdNewsUrl = new URI("/news/" + createdNewsDTO.getId());
                resp.addHeader("Location", createdNewsUrl.getPath());
            } catch (URISyntaxException e) {
                log.error("Error when creating the address of the added news in location header", e);
                throw new CustomWebRuntimeException();
            }

            HttpServletResponseUtils.writePayloadIntoResponseBody(resp, responseBody, SC_CREATED);
        } catch (CustomWebException e) {
            BaseResponseDTO responseBody = new BaseResponseDTO(e);
            HttpServletResponseUtils.writePayloadIntoResponseBody(resp, responseBody, e.getHttpStatus());
        } catch (CustomWebRuntimeException e) {
            BaseResponseDTO responseBody = new BaseResponseDTO(e);
            HttpServletResponseUtils.writePayloadIntoResponseBody(resp, responseBody, e.getHttpStatus());
        }  catch (RuntimeException e) {
            log.error("Error processing a request to add news", e);
            BaseResponseDTO responseBody = new BaseResponseDTO(e);
            HttpServletResponseUtils.writePayloadIntoResponseBody(resp, responseBody, SC_INTERNAL_SERVER_ERROR);
        }
    }
}
