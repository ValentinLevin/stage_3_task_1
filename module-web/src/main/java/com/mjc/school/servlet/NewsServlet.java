package com.mjc.school.servlet;

import com.mjc.school.dto.*;
import com.mjc.school.exception.CustomWebException;
import com.mjc.school.exception.repository.CustomException;
import com.mjc.school.util.HttpServletRequestUtils;
import com.mjc.school.util.HttpServletResponseUtils;
import com.mjc.school.service.NewsService;
import com.sun.net.httpserver.Headers;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpHeaders;
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
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            int limit = HttpServletRequestUtils.getLimitValueFromRequest(req);
            int offset = HttpServletRequestUtils.getOffsetValueFromRequest(req);
            List<NewsDTO> news = this.newsService.findAll(offset, limit);
            long totalCount = this.newsService.count();
            GetNewsListResponseDTO responseBody = new GetNewsListResponseDTO(news, offset+1, news.size(), totalCount);

            HttpServletResponseUtils.writePayloadIntoResponseBody(resp, responseBody, SC_OK);
        } catch (CustomWebException e) {
            BaseResponseDTO responseBody = new BaseResponseDTO(e);
            HttpServletResponseUtils.writePayloadIntoResponseBody(resp, responseBody, e.getHttpStatus());
        } catch (CustomException e) {
            BaseResponseDTO responseBody = new BaseResponseDTO(e);
            HttpServletResponseUtils.writePayloadIntoResponseBody(resp, responseBody, SC_INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            log.error("Error when processing a request to get a list of news", e);
            BaseResponseDTO responseBody = new BaseResponseDTO(e);
            HttpServletResponseUtils.writePayloadIntoResponseBody(resp, responseBody, SC_INTERNAL_SERVER_ERROR);
        }

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        try {
            EditNewsRequestDTO newsDTO = HttpServletRequestUtils.readObjectFromRequestBody(req, EditNewsRequestDTO.class);
            NewsDTO createdNewsDTO = newsService.add(newsDTO);
            AddNewsResponseDTO responseBody = new AddNewsResponseDTO(createdNewsDTO);

            URI createdNewsUrl = new URI("/news/" + createdNewsDTO.getId());
            resp.addHeader("Location", createdNewsUrl.getPath());

            HttpServletResponseUtils.writePayloadIntoResponseBody(resp, responseBody, SC_CREATED);
        } catch (CustomWebException e) {
            BaseResponseDTO responseBody = new BaseResponseDTO(e);
            HttpServletResponseUtils.writePayloadIntoResponseBody(resp, responseBody, e.getHttpStatus());
        } catch (CustomException e) {
            BaseResponseDTO responseBody = new BaseResponseDTO(e);
            HttpServletResponseUtils.writePayloadIntoResponseBody(resp, responseBody, SC_INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            log.error("Error processing a request to add news", e);
            BaseResponseDTO responseBody = new BaseResponseDTO(e);
            HttpServletResponseUtils.writePayloadIntoResponseBody(resp, responseBody, SC_INTERNAL_SERVER_ERROR);
        }
    }
}
