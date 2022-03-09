package io.github.cwireset.tcc.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

//sem uso

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ExceptionStatus404 extends Exception  {
    public ExceptionStatus404(String message) {
        super(message);
        }
    }
