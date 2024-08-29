package com.topview.file.common.exception;

/**
 * @Description:
 * @Author: zbj
 * @Date: 2024/8/29
 */
public class businessException extends RuntimeException{
    private static final long serialVersionUID = 1L;

    private String message;

    public businessException(String message) {
        super(message);
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
