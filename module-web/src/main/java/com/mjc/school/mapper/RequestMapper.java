package com.mjc.school.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.mjc.school.exception.IllegalDataFormatException;
import com.mjc.school.exception.NotUTFEncodingException;
import com.mjc.school.exception.RequestBodyReadException;
import jakarta.servlet.http.HttpServletRequest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class RequestMapper {
    private static final ObjectMapper mapper = new JsonMapper().findAndRegisterModules();

    public static <T> T readObjectFromRequestBody(HttpServletRequest request, Class<T> clazz) {

        try {
            request.setCharacterEncoding("UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new NotUTFEncodingException();
        }

        StringBuilder builder = new StringBuilder();
        String line;
        try (BufferedReader reader = request.getReader()) {
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }
        } catch (IOException e) {
            throw new RequestBodyReadException();
        }

        try {
            return mapper.readValue(builder.toString(), clazz);
        } catch (JsonProcessingException e) {
            throw new IllegalDataFormatException(builder.toString(), clazz);
        }
    }
}
