package com.mjc.school.servlet;

import com.mjc.school.dto.*;
import com.mjc.school.exception.CustomWebException;
import com.mjc.school.exception.IllegalLimitValueException;
import com.mjc.school.exception.repository.CustomException;
import com.mjc.school.exception.repository.UnexpectedErrorException;
import com.mjc.school.util.HttpServletRequestUtils;
import com.mjc.school.util.ResponseMapper;
import com.mjc.school.service.NewsService;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

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
        Object responseBody;

        try {
            int limit = HttpServletRequestUtils.getLimitValueFromRequest(req);
            int offset = HttpServletRequestUtils.getOffsetValueFromRequest(req);
            List<NewsDTO> news = this.newsService.findAll();
            long totalCount = this.newsService.count();
            responseBody = new GetNewsListResponseDTO(news, offset+1, news.size(), totalCount);
                    ResponseMapper.writePayloadIntoResponseBody(resp, responseBody, SC_OK);
        } catch (CustomWebException e) {

        } catch (CustomException e) {
            responseBody = new BaseResponseDTO(e);
        } catch (Exception e) {
            log.error("Error when processing a request to get a list of news", e);
            responseBody = new BaseResponseDTO(e);
        }

    }


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        Object responseBody;
        try {
            EditNewsRequestDTO newsDTO = HttpServletRequestUtils.readObjectFromRequestBody(req, EditNewsRequestDTO.class);
            NewsDTO savedNewsDTO = newsService.add(newsDTO);
            responseBody = new AddNewsResponseDTO(savedNewsDTO);
            ResponseMapper.writePayloadIntoResponseBody(resp, responseBody, SC_CREATED);
        } catch (CustomWebException e) {
            responseBody = new BaseResponseDTO(e);
            ResponseMapper.writePayloadIntoResponseBody(resp, responseBody, e.getHttpStatus());
        } catch (CustomException e) {
            responseBody = new BaseResponseDTO(e);
            ResponseMapper.writePayloadIntoResponseBody(resp, responseBody, SC_INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            log.error("Error processing a request to add news", e);
            responseBody = new BaseResponseDTO(e);
            ResponseMapper.writePayloadIntoResponseBody(resp, responseBody, SC_INTERNAL_SERVER_ERROR);
        }
    }
}
