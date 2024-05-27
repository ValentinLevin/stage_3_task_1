package com.mjc.school.web.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mjc.school.web.exception.CustomWebException;
import com.mjc.school.web.constant.RESULT_CODE;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BaseResponseDTO {
    @JsonProperty("errorCode")
    private final int errorCode;

    @JsonProperty("errorMessage")
    private final String errorMessage;

    public BaseResponseDTO(
            int errorCode,
            String errorMessage
    ) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public BaseResponseDTO() {
        this(
                RESULT_CODE.SUCCESS.getErrorCode(),
                null
        );
    }

    public BaseResponseDTO(int errorCode) {
        this(
                errorCode,
                null
        );
    }

    public BaseResponseDTO(CustomWebException exception) {
        this(
                exception.getErrorCode().getErrorCode(),
                exception.getMessage()
        );
    }

    public BaseResponseDTO(Throwable throwable) {
        this(
                RESULT_CODE.UNEXPECTED_ERROR.getErrorCode(),
                throwable.getMessage()
        );
    }

    public BaseResponseDTO(RESULT_CODE resultCode, Throwable throwable) {
        this(resultCode.getErrorCode(), throwable.getMessage());
    }
}
