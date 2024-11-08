package com.iflove.starter.frequencycontrol.exception;

import com.iflove.starter.frequencycontrol.exception.errorcode.BaseErrorCode;
import com.iflove.starter.frequencycontrol.exception.errorcode.ErrorCode;

/**
 * @author 苍镜月
 * @version 1.0
 * @implNote 远程调用异常
 */

public class RemoteException extends AbstractException {
    public RemoteException(String message) {
        this(message, null, BaseErrorCode.REMOTE_ERROR);
    }

    public RemoteException(String message, ErrorCode errorCode) {
        this(message, null, errorCode);
    }

    public RemoteException(String message, Throwable throwable, ErrorCode errorCode) {
        super(message, throwable, errorCode);
    }

    @Override
    public String toString() {
        return "RemoteException{" +
                "code='" + errorCode + "'," +
                "message='" + errorMessage + "'" +
                '}';
    }
}
