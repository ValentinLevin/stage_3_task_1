package com.mjc.school.servlet;

import com.mjc.school.dto.AddNewsResponseDTO;
import com.mjc.school.dto.EditNewsRequestDTO;
import com.mjc.school.dto.NewsDTO;
import com.mjc.school.mapper.RequestMapper;
import com.mjc.school.mapper.ResponseMapper;
import com.mjc.school.service.NewsService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

import static jakarta.servlet.http.HttpServletResponse.SC_CREATED;
import static jakarta.servlet.http.HttpServletResponse.SC_OK;

@WebServlet("/news")
public class NewsServlet extends HttpServlet {
    private final NewsService newsService;

    public NewsServlet(NewsService newsService) {
        this.newsService = newsService;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        List<NewsDTO> news = this.newsService.findAll();
        ResponseMapper.writePayloadIntoResponseBody(resp, news, SC_OK);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        EditNewsRequestDTO newsDTO = RequestMapper.readObjectFromRequestBody(req, EditNewsRequestDTO.class);

        NewsDTO savedNewsDTO = newsService.add(newsDTO);
        AddNewsResponseDTO responseBody = new AddNewsResponseDTO(savedNewsDTO);

        ResponseMapper.writePayloadIntoResponseBody(resp, responseBody, SC_CREATED);
    }
}
