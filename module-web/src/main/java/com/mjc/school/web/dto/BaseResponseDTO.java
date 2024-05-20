package com.mjc.school.web.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mjc.school.web.exception.CustomWebException;
import com.mjc.school.web.constant.ERROR_CODE;
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
                ERROR_CODE.NO_ERROR.getId(),
                null
        );
    }

    public BaseResponseDTO(CustomWebException exception) {
        this(
                exception.getErrorCode().getId(),
                exception.getMessage()
        );
    }

    public BaseResponseDTO(Throwable throwable) {
        this(
                ERROR_CODE.UNEXPECTED_ERROR.getId(),
                throwable.getMessage()
        );
    }
}
