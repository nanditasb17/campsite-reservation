package com.volcano.campsite.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CampsiteException extends RuntimeException {

    private CampsiteErrorCode campsiteErrorCode;

}
