package com.mjc.school.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.mjc.school.service.dto.AuthorDTO;
import com.mjc.school.service.dto.EditNewsRequestDTO;
import com.mjc.school.service.dto.NewsDTO;
import com.mjc.school.service.service.NewsService;
import com.mjc.school.web.dto.AddNewsResponseDTO;
import com.mjc.school.web.servlet.NewsServlet;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.tuple;

@ExtendWith(MockitoExtension.class)
class AddNewNewsTest {
    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private NewsService newsService;

    private ByteArrayOutputStream responseBodyStream;
    private final ObjectMapper mapper = new JsonMapper().findAndRegisterModules();

    @BeforeEach
    void setup() throws IOException {
        Mockito.when(request.getMethod()).thenReturn("POST");

        responseBodyStream = new ByteArrayOutputStream();
        PrintWriter writer = new PrintWriter(responseBodyStream);
        Mockito.when(response.getWriter()).thenReturn(writer);
    }

    @Test
    @DisplayName("Adding news. Successful, no errors")
    void createNewsTest() throws IOException, ServletException {
        String requestBody =
                "{ " +
                        "\"title\" : \"News title\", " +
                        "\"content\": \"News content\", " +
                        "\"authorId\": 12" +
                        "}";

        StringReader stringReader = new StringReader(requestBody);
        BufferedReader reader = new BufferedReader(stringReader);

        Mockito.when(request.getReader()).thenReturn(reader);

        EditNewsRequestDTO exceptedRequestDTO = new EditNewsRequestDTO(
                "News title",
                "News content",
                12L
        );

        NewsDTO createdNews = new NewsDTO(
                123L,
                exceptedRequestDTO.getTitle(),
                exceptedRequestDTO.getContent(),
                "",
                "",
                new AuthorDTO(exceptedRequestDTO.getAuthorId(), "")
        );
        Mockito.when(newsService.add(Mockito.any(EditNewsRequestDTO.class))).thenReturn(createdNews);

        new NewsServlet(newsService).service(request, response);

        ArgumentCaptor<EditNewsRequestDTO> editNewsRequestDTOArgumentCaptor = ArgumentCaptor.forClass(EditNewsRequestDTO.class);
        Mockito.verify(newsService).add(editNewsRequestDTOArgumentCaptor.capture());

        EditNewsRequestDTO actualRequestDTO = editNewsRequestDTOArgumentCaptor.getValue();

        assertThat(actualRequestDTO).isEqualTo(exceptedRequestDTO);
        Mockito.verify(response).setStatus(HttpServletResponse.SC_CREATED);
        Mockito.verify(response).addHeader("Location", "/news/" + createdNews.getId());

        assertThat(responseBodyStream.size()).isNotZero();
        AddNewsResponseDTO responseBody = mapper.readValue(responseBodyStream.toByteArray(), AddNewsResponseDTO.class);

        assertThat(responseBody.getErrorCode()).isZero();
        assertThat(responseBody.getErrorMessage()).isNullOrEmpty();
        assertThat(responseBody.getData())
                .isNotNull()
                .extracting("title", "content", "author.id")
                .containsExactly(
                        exceptedRequestDTO.getTitle(),
                        exceptedRequestDTO.getContent(),
                        exceptedRequestDTO.getAuthorId()
                );
    }

    @Test
    @DisplayName("Добавление новости. Не переданы данные новости в теле запроса")
    void noBody() throws IOException, ServletException {
        StringReader reader = new StringReader("");
        BufferedReader bufferedReader = new BufferedReader(reader);
        Mockito.when(request.getReader()).thenReturn(bufferedReader);

        new NewsServlet(newsService).service(request, response);

    }
}
