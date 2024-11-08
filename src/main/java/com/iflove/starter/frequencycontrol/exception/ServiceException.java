package com.iflove.starter.frequencycontrol.exception;

import com.iflove.starter.frequencycontrol.exception.errorcode.BaseErrorCode;
import com.iflove.starter.frequencycontrol.exception.errorcode.ErrorCode;

import java.util.Optional;

/**
 * @author 苍镜月
 * @version 1.0
 * @implNote 服务端异常
 */

public class ServiceException extends AbstractException {
    public ServiceException(String message) {
        this(message, null, BaseErrorCode.SERVICE_ERROR);
    }

    public ServiceException(ErrorCode errorCode) {
        this(null, errorCode);
    }

    public ServiceException(String message, ErrorCode errorCode) {
        this(message, null, errorCode);
    }

    public ServiceException(String message, Throwable throwable, ErrorCode errorCode) {
        super(Optional.ofNullable(message).orElse(errorCode.message()), throwable, errorCode);
    }

    @Override
    public String toString() {
        return "ServiceException{" +
                "code='" + errorCode + "'," +
                "message='" + errorMessage + "'" +
                '}';
    }
}
