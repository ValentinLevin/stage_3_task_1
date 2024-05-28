package com.mjc.school.web.servlet;

import com.mjc.school.service.dto.EditNewsRequestDTO;
import com.mjc.school.service.dto.NewsDTO;
import com.mjc.school.service.exception.*;
import com.mjc.school.service.service.NewsService;
import com.mjc.school.web.constant.RESULT_CODE;
import com.mjc.school.web.dto.BaseResponseDTO;
import com.mjc.school.web.dto.GetNewsItemResponseDTO;
import com.mjc.school.web.dto.UpdateNewsResponseDTO;
import com.mjc.school.web.exception.*;
import com.mjc.school.web.mapper.ResultCodeMapper;
import com.mjc.school.web.util.HttpServletRequestUtils;
import com.mjc.school.web.util.HttpServletResponseUtils;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import static jakarta.servlet.http.HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
import static jakarta.servlet.http.HttpServletResponse.SC_OK;

@WebServlet("/news/*")
@Slf4j
public class NewsItemServlet extends HttpServlet {
    private final transient NewsService newsService;

    public NewsItemServlet(NewsService newsService) {
        this.newsService = newsService;
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) {
        long newsId = 0;
        BaseResponseDTO responseBody;
        RESULT_CODE resultCode;
        try {
            newsId = HttpServletRequestUtils.getIdFromPath(req);

            EditNewsRequestDTO newsDTO =
                    HttpServletRequestUtils.readObjectFromRequestBody(req, EditNewsRequestDTO.class);
            NewsDTO editedNewsDTO = newsService.update(newsId, newsDTO);
            responseBody = new UpdateNewsResponseDTO(editedNewsDTO);
            resultCode = RESULT_CODE.SUCCESS;
        } catch (IllegalNewsIdValueWebException | NotUTFEncodingWebException | NoDataInRequestWebException |
                 IllegalDataFormatWebException e)
        {
            resultCode = ResultCodeMapper.getResultCode(e.getClass());
            responseBody = new BaseResponseDTO(e);

        } catch (DTOValidationServiceException | NullNewsIdServiceException | NewsNotFoundServiceException |
                 NullAuthorIdServiceException | AuthorNotFoundServiceException e)
        {

        } catch (CustomWebRuntimeException e) {
            resultCode = ResultCodeMapper.getResultCode(e.getClass());
            responseBody = new BaseResponseDTO(e);
        } catch (RuntimeException e) {
            log.error("Error when processing a request to change news data with id {}", newsId, e);
            resultCode = RESULT_CODE.UNEXPECTED_ERROR;
            responseBody = new BaseResponseDTO(resultCode.getErrorCode(), resultCode.getDefaultMessage());
        }

        HttpServletResponseUtils.writePayloadIntoResponseBody(resp, responseBody, resultCode.getHttpStatus());
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        long newsId = 0;
        try {
            newsId = HttpServletRequestUtils.getIdFromPath(req);

            NewsDTO newsDTO;
            try {
                newsDTO = newsService.findById(newsId);
            } catch (NullAuthorIdServiceException | AuthorNotFoundServiceException e) {
                log.error("Unexpected error when requesting news by id {}", newsId, e);
                throw new IllegalNewsIdValueWebException("null");
            } catch (NullNewsIdServiceException e) {
                throw new IllegalNewsIdValueWebException("null");
            } catch (NewsNotFoundServiceException e) {
                throw new NewsNotFoundWebException(newsId);
            }
            GetNewsItemResponseDTO responseBody = new GetNewsItemResponseDTO(newsDTO);
            HttpServletResponseUtils.writePayloadIntoResponseBody(resp, responseBody, SC_OK);
        } catch (CustomWebException e) {
            BaseResponseDTO responseBody = new BaseResponseDTO(e);
            HttpServletResponseUtils.writePayloadIntoResponseBody(resp, responseBody, e.getHttpStatus());
        } catch (RuntimeException e) {
            log.error("Error when requesting news by id {}", newsId, e);
            BaseResponseDTO responseBody = new BaseResponseDTO(e);
            HttpServletResponseUtils.writePayloadIntoResponseBody(resp, responseBody, SC_INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) {
        long newsId = 0;
        try {
            newsId = HttpServletRequestUtils.getIdFromPath(req);
            try {
                newsService.deleteById(newsId);
            } catch (NullNewsIdServiceException e) {
                throw new IllegalNewsIdValueWebException("null");
            } catch (NewsNotFoundServiceException e) {
                throw new NewsNotFoundWebException(newsId);
            }
            HttpServletResponseUtils.writePayloadIntoResponseBody(resp, new BaseResponseDTO(), SC_OK);
        } catch (CustomWebException e) {
            BaseResponseDTO responseBody = new BaseResponseDTO(e);
            HttpServletResponseUtils.writePayloadIntoResponseBody(resp, responseBody, e.getHttpStatus());
        } catch (RuntimeException e) {
            log.error("Error when deleting news by id {}", newsId, e);
            BaseResponseDTO responseBody = new BaseResponseDTO(e);
            HttpServletResponseUtils.writePayloadIntoResponseBody(resp, responseBody, SC_INTERNAL_SERVER_ERROR);
        }
    }
}
