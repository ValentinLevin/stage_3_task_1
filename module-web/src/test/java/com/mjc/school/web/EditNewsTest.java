package com.mjc.school.web;

import com.mjc.school.service.dto.AuthorDTO;
import com.mjc.school.service.dto.EditNewsRequestDTO;
import com.mjc.school.service.dto.NewsDTO;
import com.mjc.school.service.exception.CustomServiceException;
import com.mjc.school.service.service.NewsService;
import com.mjc.school.web.dto.AddNewsResponseDTO;
import com.mjc.school.web.servlet.NewsServlet;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.io.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class EditNewsTest {
    private HttpServletRequest request;
    private HttpServletResponse response;
    private NewsService newsService;

    private ByteArrayOutputStream responseBodyStream;

    @BeforeEach
    void setup() throws IOException {
        newsService = Mockito.mock(NewsService.class);
        request = Mockito.mock(HttpServletRequest.class);
        response = Mockito.mock(HttpServletResponse.class);

        Mockito.when(request.getMethod()).thenReturn("POST");

        responseBodyStream = new ByteArrayOutputStream();
        PrintWriter writer = new PrintWriter(responseBodyStream);
        Mockito.when(response.getWriter()).thenReturn(writer);
    }

    @Test
    @DisplayName("Editing news. Successful, no errors")
    void editNewsSuccessTest() throws IOException, ServletException, CustomServiceException {
        long idForUpdate = 123L;

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

        Mockito.when(request.getPathInfo()).thenReturn(String.valueOf(idForUpdate));
        Mockito.when(newsService.findById(idForUpdate)).thenReturn();

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

}
