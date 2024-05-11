package com.mjc.school.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.mjc.school.exception.repository.UnexpectedErrorException;
import jakarta.servlet.http.HttpServletResponse;

import java.io.PrintWriter;

import static jakarta.servlet.http.HttpServletResponse.SC_OK;

public class ResponseMapper {
    private static final ObjectMapper mapper = new JsonMapper().findAndRegisterModules();

    public static <T> void writePayloadIntoResponseBody(HttpServletResponse response, T value, int status) throws JsonProcessingException {
        try {
            String valueAsString = mapper.writeValueAsString(value);
            response.setStatus(status);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");

            PrintWriter writer = response.getWriter();
            writer.write(valueAsString);
        } catch (Exception e) {
            throw new UnexpectedErrorException(e);
        }
    }
}
