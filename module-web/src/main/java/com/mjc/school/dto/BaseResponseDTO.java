package com.mjc.school.dto;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mjc.school.exception.repository.CustomException;
import com.mjc.school.exception.repository.ERROR_CODE;
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

    public BaseResponseDTO(CustomException exception) {
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
