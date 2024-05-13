package com.mjc.school.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.mjc.school.exception.repository.UnexpectedErrorException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import java.io.PrintWriter;

@Slf4j
public class HttpServletResponseUtils {
    private static final ObjectMapper mapper = new JsonMapper().findAndRegisterModules();

    public static <T> void writePayloadIntoResponseBody(HttpServletResponse response, T value, int status) {
        try {
            String valueAsString = mapper.writeValueAsString(value);

            response.setStatus(status);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");

            try (PrintWriter writer = response.getWriter()) {
                writer.write(valueAsString);
            }
        } catch (Exception e) {
            log.error("Error when writing the result of request processing in response", e);
            throw new UnexpectedErrorException(e);
        }
    }
}
