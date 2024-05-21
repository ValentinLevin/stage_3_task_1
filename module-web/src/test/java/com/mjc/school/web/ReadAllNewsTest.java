package com.mjc.school.web;

import com.mjc.school.service.dto.AuthorDTO;
import com.mjc.school.service.dto.NewsDTO;
import com.mjc.school.service.service.NewsService;
import com.mjc.school.web.servlet.NewsServlet;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ReadAllNewsTest {
    private NewsService newsService;
    private HttpServletRequest request;
    private HttpServletResponse response;

    @BeforeEach
    void setUp() {
        newsService = Mockito.mock(NewsService.class);
        request = Mockito.mock(HttpServletRequest.class);
        response = Mockito.mock(HttpServletResponse.class);
    }

    @Test
    void test() throws IOException, ServletException {
        List<NewsDTO> news = List.of(
                new NewsDTO(
                        1L,
                        "News 1 title",
                        "News 1 content",
                        new AuthorDTO(
                                11L,
                                "Author 1 name"
                        )
                ),
                new NewsDTO(
                        2L,
                        "News 2 title",
                        "News 2 content",
                        new AuthorDTO(
                                22L,
                                "Author 2 name"
                        )
                ),
                new NewsDTO(
                        3L,
                        "News 3 title",
                        "News 3 content",
                        new AuthorDTO(
                                33L,
                                "Author 3 name"
                        )
                )
        );

        Mockito.when(newsService.findAll(Mockito.anyLong(), Mockito.anyLong())).thenReturn(news);

        Mockito.when(request.getParameter("limit")).thenReturn(null);
        Mockito.when(request.getParameter("offset")).thenReturn(null);
        Mockito.when(request.getMethod()).thenReturn("GET");

        ByteArrayOutputStream responseStream = new ByteArrayOutputStream();
        PrintWriter printWriter = new PrintWriter(responseStream);
        Mockito.when(response.getWriter()).thenReturn(printWriter);

        new NewsServlet(newsService).service(request, response);

        assertThat(response.getStatus()).isEqualTo(200);
    }
}
