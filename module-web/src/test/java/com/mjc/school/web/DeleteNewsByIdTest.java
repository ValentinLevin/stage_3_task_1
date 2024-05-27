package com.mjc.school.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.mjc.school.service.exception.CustomServiceException;
import com.mjc.school.service.exception.NewsNotFoundServiceException;
import com.mjc.school.service.service.NewsService;
import com.mjc.school.web.constant.RESULT_CODE;
import com.mjc.school.web.dto.BaseResponseDTO;
import com.mjc.school.web.exception.IllegalAuthorIdValueWebException;
import com.mjc.school.web.exception.NewsNotFoundWebException;
import com.mjc.school.web.servlet.NewsItemServlet;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class DeleteNewsByIdTest {
    private HttpServletRequest request;
    private HttpServletResponse response;
    private NewsService newsService;

    private ByteArrayOutputStream responseBodyStream;
    private final ObjectMapper mapper = new JsonMapper().findAndRegisterModules();

    @BeforeEach
    void setup() throws IOException {
        newsService = Mockito.mock(NewsService.class);
        request = Mockito.mock(HttpServletRequest.class);
        response = Mockito.mock(HttpServletResponse.class);

        Mockito.when(request.getMethod()).thenReturn("DELETE");

        responseBodyStream = new ByteArrayOutputStream();
        PrintWriter writer = new PrintWriter(responseBodyStream);
        Mockito.when(response.getWriter()).thenReturn(writer);
    }

    @Test()
    @DisplayName("Successful deletion of news")
    void successTest() throws ServletException, IOException, CustomServiceException {
        Mockito.when(request.getPathInfo()).thenReturn("/12");

        Mockito.when(newsService.deleteById(Mockito.anyLong())).thenReturn(true);

        new NewsItemServlet(newsService).service(request, response);

        Mockito.verify(response).setStatus(HttpServletResponse.SC_OK);

        BaseResponseDTO actualResponse = mapper.readValue(responseBodyStream.toByteArray(), BaseResponseDTO.class);
        assertThat(actualResponse.getErrorCode()).isEqualTo(RESULT_CODE.GET_SUCCESS.getErrorCode());
    }

    @Test
    @DisplayName("Удаление новости. Не найдена новость")
    void notFoundById() throws ServletException, IOException, CustomServiceException {
        Mockito.when(request.getPathInfo()).thenReturn("/1");
        Mockito.when(newsService.deleteById(Mockito.anyLong())).thenThrow(NewsNotFoundServiceException.class);

        new NewsItemServlet(newsService).service(request, response);

        NewsNotFoundWebException expectedException = new NewsNotFoundWebException(1L);

        Mockito.verify(response).setStatus(expectedException.getHttpStatus());

        assertThat(responseBodyStream.size()).isNotZero();
        BaseResponseDTO actualResponseBody = mapper.readValue(responseBodyStream.toByteArray(), BaseResponseDTO.class);
        assertThat(actualResponseBody.getErrorCode()).isEqualTo(expectedException.getErrorCode().getErrorCode());
    }

    @Test
    @DisplayName("Удаление новости. Формат id некорректный")
    void incorrectId() throws ServletException, IOException {
        Mockito.when(request.getPathInfo()).thenReturn("/1_1");

        new NewsItemServlet(newsService).service(request, response);

        IllegalAuthorIdValueWebException expectedException = new IllegalAuthorIdValueWebException("1_1");

        Mockito.verify(response).setStatus(expectedException.getHttpStatus());

        assertThat(responseBodyStream.size()).isNotZero();
        BaseResponseDTO actualResponseBody = mapper.readValue(responseBodyStream.toByteArray(), BaseResponseDTO.class);
        assertThat(actualResponseBody.getErrorCode()).isEqualTo(expectedException.getErrorCode().getErrorCode());
    }
}
