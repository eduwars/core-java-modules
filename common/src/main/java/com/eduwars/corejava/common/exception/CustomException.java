package com.eduwars.corejava.common.exception;

import com.eduwars.corejava.common.error.ErrorCode;
import com.eduwars.corejava.common.logger.CustomLogger;
import lombok.Getter;

/**
 * @author Ravi Singh
 * @date 04/06/20-06-2020
 * @project eduwars
 */
public class CustomException extends RuntimeException {

    @Getter
    private ErrorCode errorCode;

    public CustomException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }


    public static CustomException wrap(Throwable ex) {
        /* this will help in finding out errors which are not known.. like null pointer.. IO exception... which are not in ErrorCodes list*/
        if(!(ex instanceof CustomException)) {
            CustomLogger.error("Unknown Custom Error::"+ex.getMessage(), ex);
        }
        return ex instanceof CustomException ? (CustomException) ex : new CustomException(ErrorCode.INTERNAL_ERROR);
    }

}