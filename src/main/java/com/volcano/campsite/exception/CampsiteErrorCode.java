package com.volcano.campsite.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum CampsiteErrorCode {

    DATES_NOT_AVAILABLE("DATES_NOT_AVAILABLE", HttpStatus.BAD_REQUEST, "The requested dates are no longer available"),
    RESERVATION_NOT_FOUND("RESERVATION_NOT_FOUND", HttpStatus.NOT_FOUND, "The requested reservation id is not present"),
    DATE_RANGE_NOT_VALID("DATE_RANGE_NOT_VALID", HttpStatus.BAD_REQUEST, "The date range in the request does not meet system requirements");

    private String errorCode;
    private HttpStatus status;
    private String message;

}
