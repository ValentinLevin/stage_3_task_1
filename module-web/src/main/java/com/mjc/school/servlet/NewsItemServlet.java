package com.mjc.school.servlet;

import com.mjc.school.dto.*;
import com.mjc.school.exception.CustomWebException;
import com.mjc.school.exception.NewsNotFoundException;
import com.mjc.school.exception.repository.CustomException;
import com.mjc.school.service.NewsService;
import com.mjc.school.util.HttpServletRequestUtils;
import com.mjc.school.util.HttpServletResponseUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

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
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        long id = HttpServletRequestUtils.getIdFromPath(req, 1);
        try {
            EditNewsRequestDTO newsDTO = HttpServletRequestUtils.readObjectFromRequestBody(req, EditNewsRequestDTO.class);
            NewsDTO editedNewsDTO = newsService.update(id, newsDTO);
            UpdateNewsResponseDTO responseBody = new UpdateNewsResponseDTO(editedNewsDTO);
            HttpServletResponseUtils.writePayloadIntoResponseBody(resp, responseBody, SC_OK);
        } catch (CustomWebException e) {
            BaseResponseDTO responseBody = new BaseResponseDTO(e);
            HttpServletResponseUtils.writePayloadIntoResponseBody(resp, responseBody, e.getHttpStatus());
        } catch (Exception e) {
            log.error("Error when processing a request to change news data with id {}", id, e);
            BaseResponseDTO responseBody = new BaseResponseDTO(e);
            HttpServletResponseUtils.writePayloadIntoResponseBody(resp, responseBody, SC_INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        long id = HttpServletRequestUtils.getIdFromPath(req, 1);
        try {
            NewsDTO newsDTO = newsService.findById(id);
            GetNewsItemResponseDTO responseBody = new GetNewsItemResponseDTO(newsDTO);
            HttpServletResponseUtils.writePayloadIntoResponseBody(resp, responseBody, SC_OK);
        } catch (NewsNotFoundException e) {
            BaseResponseDTO responseBody = new BaseResponseDTO(e);
            HttpServletResponseUtils.writePayloadIntoResponseBody(resp, responseBody, e.getHttpStatus());
        } catch (Exception e) {
            log.error("Error when requesting news by id {}", id, e);
            BaseResponseDTO responseBody = new BaseResponseDTO(e);
            HttpServletResponseUtils.writePayloadIntoResponseBody(resp, responseBody, SC_INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) {
        long id = HttpServletRequestUtils.getIdFromPath(req, 1);
        try {
            newsService.deleteById(id);
            HttpServletResponseUtils.writePayloadIntoResponseBody(resp, new BaseResponseDTO(), SC_OK);
        } catch (NewsNotFoundException e) {
            BaseResponseDTO responseBody = new BaseResponseDTO(e);
            HttpServletResponseUtils.writePayloadIntoResponseBody(resp, responseBody, e.getHttpStatus());
        } catch (Exception e) {
            log.error("Error when deleting news by id {}", id, e);
            BaseResponseDTO responseBody = new BaseResponseDTO(e);
            HttpServletResponseUtils.writePayloadIntoResponseBody(resp, responseBody, SC_INTERNAL_SERVER_ERROR);
        }
    }
}
