package com.iflove.starter.frequencycontrol.exception;

import com.iflove.starter.frequencycontrol.exception.errorcode.BaseErrorCode;
import com.iflove.starter.frequencycontrol.exception.errorcode.ErrorCode;
import lombok.Getter;

/**
 * @author 苍镜月
 * @version 1.0
 * @implNote 限流异常
 */

public class FrequencyControlException extends AbstractException {
    public FrequencyControlException(ErrorCode errorCode) {
        this(null, null, errorCode);
    }

    public FrequencyControlException(String message) {
        this(message, null, BaseErrorCode.FREQUENCY_LIMIT);
    }

    public FrequencyControlException(String message, ErrorCode errorCode) {
        this(message, null, errorCode);
    }

    public FrequencyControlException(String message, Throwable throwable, ErrorCode errorCode) {
        super(message, throwable, errorCode);
    }

    @Override
    public String toString() {
        return "FrequencyControlException{" +
                "code='" + errorCode + "'," +
                "message='" + errorMessage + "'" +
                '}';
    }
}
