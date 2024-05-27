package com.mjc.school.web.mapper;

import com.mjc.school.service.exception.*;
import com.mjc.school.web.constant.RESULT_CODE;
import com.mjc.school.web.exception.CustomWebRuntimeException;

import java.util.HashMap;
import java.util.Map;

public class ResultCodeMapper {
    private static final Map<Class<?>, RESULT_CODE> errorCodes = new HashMap<>();

    static {
        errorCodes.put(AuthorNotFoundServiceException.class, RESULT_CODE.AUTHOR_NOT_FOUND);
        errorCodes.put(NewsNotFoundServiceException.class, RESULT_CODE.NEWS_NOT_FOUND);
        errorCodes.put(DTOValidationServiceException.class, RESULT_CODE.DATA_VALIDATION);
        errorCodes.put(NullAuthorIdServiceException.class, RESULT_CODE.DATA_VALIDATION);
        errorCodes.put(NullNewsIdServiceException.class, RESULT_CODE.ILLEGAL_ID_VALUE);
        errorCodes.put(CustomServiceException.class, RESULT_CODE.UNEXPECTED_ERROR);
        errorCodes.put(CustomServiceRuntimeException.class, RESULT_CODE.UNEXPECTED_ERROR);
        errorCodes.put(CustomWebRuntimeException.class, RESULT_CODE.UNEXPECTED_ERROR);
    }

    private ResultCodeMapper() {}

    public static RESULT_CODE getResultCode(Class<?> clazz) {
        RESULT_CODE resultCode = errorCodes.get(clazz);
        if (resultCode == null) {
            resultCode = errorCodes.get(CustomWebRuntimeException.class);
        }
        return resultCode;
    }
}
