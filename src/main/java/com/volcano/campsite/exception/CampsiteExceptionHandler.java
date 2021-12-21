package com.volcano.campsite.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;

@RestControllerAdvice
public class CampsiteExceptionHandler {

    private static Logger logger = LoggerFactory.getLogger(CampsiteExceptionHandler.class);

    @ExceptionHandler
    public ResponseEntity<CampsiteError> handleCmException(CampsiteException exception, HttpServletRequest request) {
        logger.info("Returning error={}, request={}, method={}, status={}, msg={}",
                exception.getCampsiteErrorCode(),
                request.getRequestURI(),
                request.getMethod(),
                exception.getCampsiteErrorCode().getStatus(), exception.getCampsiteErrorCode().getMessage());
        return new ResponseEntity<>(
                new CampsiteError(LocalDate.now(),
                        exception.getCampsiteErrorCode().getStatus().value(),
                        exception.getCampsiteErrorCode().getErrorCode(),
                        exception.getCampsiteErrorCode().getMessage(),
                        request.getRequestURI()),
                exception.getCampsiteErrorCode().getStatus()
        );
    }

}
