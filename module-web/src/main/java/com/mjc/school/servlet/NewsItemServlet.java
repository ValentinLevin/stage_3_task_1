package com.mjc.school.servlet;

import com.mjc.school.dto.BaseResponseDTO;
import com.mjc.school.dto.EditNewsRequestDTO;
import com.mjc.school.dto.NewsDTO;
import com.mjc.school.service.NewsService;

import com.mjc.school.service.NewsServiceFactory;
import com.mjc.school.util.HttpServletRequestUtils;
import com.mjc.school.util.HttpServletResponseUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
        try {
            EditNewsRequestDTO newsDTO = HttpServletRequestUtils.readObjectFromRequestBody(req, EditNewsRequestDTO.class);
            long id = HttpServletRequestUtils.getIdFromPath(req, 1);
            NewsDTO editedNewsDTO = newsService.update(id, newsDTO);
        } catch (Exception e) {
            log.error("Ошибка при обработке запроса на изменение данных новости", e);
            BaseResponseDTO responseBody = new BaseResponseDTO(e);
            HttpServletResponseUtils.writePayloadIntoResponseBody(resp, responseBody, SC_INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            long id = HttpServletRequestUtils.getIdFromPath(req, 1);
            NewsDTO newsDTO = newsService.findById(id);
            HttpServletResponseUtils.writePayloadIntoResponseBody(resp, newsDTO, SC_OK);
        } catch (Fou)
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("doDelete");
    }
}
