package com.mjc.school.web.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.mjc.school.web.exception.*;
import jakarta.servlet.http.HttpServletRequest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Optional;

public class HttpServletRequestUtils {
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
            throw new UnexpectedErrorException(e);
        }

        try {
            return mapper.readValue(builder.toString(), clazz);
        } catch (JsonProcessingException e) {
            throw new IllegalDataFormatException(builder.toString(), clazz);
        }
    }

    public static int getLimitValueFromRequest(HttpServletRequest req) {
        String valueAsString = req.getParameter("limit");

        if (valueAsString != null) {
            int valueAsInt;
            try {
                valueAsInt = Integer.parseInt(valueAsString);
            } catch (NumberFormatException e) {
                throw new IllegalLimitValueException(valueAsString);
            }

            if (valueAsInt < 0) {
                throw new IllegalLimitValueException(valueAsInt);
            }

            return valueAsInt;
        } else {
            return -1;
        }
    }

    public static int getOffsetValueFromRequest(HttpServletRequest req) {
        String valueAsString = Optional.ofNullable(req.getParameter("offset")).orElse("0");

        int valueAsInt;
        try {
            valueAsInt = Integer.parseInt(valueAsString);
        } catch (NumberFormatException e) {
            throw new IllegalOffsetValueFormatException(valueAsString);
        }

        if (valueAsInt < 0) {
            throw new IllegalOffsetValueFormatException(valueAsInt);
        }

        return valueAsInt;
    }

    public static long getIdFromPath(HttpServletRequest req, int pathPartIndex) {
        String path = req.getPathInfo();
        return Long.parseLong(path.replace("/", ""));
    }
}
