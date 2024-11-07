package com.iflove.starter.frequencycontrol.exception;

import com.iflove.starter.frequencycontrol.exception.errorcode.ErrorCode;
import lombok.Getter;

import java.util.Objects;
import java.util.Optional;

/**
 * @author 苍镜月
 * @version 1.0
 * @implNote 抽象异常类
 */

@Getter
public abstract class AbstractException extends RuntimeException {
    public final String errorCode;

    public final String errorMessage;

    public AbstractException(String message, Throwable throwable, ErrorCode errorCode) {
        super(message, throwable);
        this.errorCode = errorCode.code();
        this.errorMessage = Optional.ofNullable(Objects.nonNull(message) && !message.isBlank() ? message : null).orElse(errorCode.message());
    }
}
