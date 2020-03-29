package com.sapient.al.exceptions;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
public class FileTypeNotSupported extends RuntimeException {

    public FileTypeNotSupported() {
    }

    public FileTypeNotSupported(String message) {
        super(message);
    }

    public FileTypeNotSupported(String message, Throwable cause) {
        super(message, cause);
    }

    public FileTypeNotSupported(Throwable cause) {
        super(cause);
    }

    public FileTypeNotSupported(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
