package com.volcano.campsite.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class CampsiteError {

    private LocalDate timestamp;
    private int status;
    private String errorCode;
    private String message;
    private String path;
}
