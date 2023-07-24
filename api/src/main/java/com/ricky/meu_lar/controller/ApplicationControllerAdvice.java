package com.ricky.meu_lar.controller;

import com.ricky.meu_lar.error.ApiError;
import com.ricky.meu_lar.exception.EmaiInvalido;
import com.ricky.meu_lar.exception.EmailJaCadastrado;
import com.ricky.meu_lar.exception.SenhaCurta;
import com.ricky.meu_lar.exception.UsuarioNaoEncontrado;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApplicationControllerAdvice {
    @ExceptionHandler(EmailJaCadastrado.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleEmailJaCadastradoException(EmailJaCadastrado ex) {
        return new ApiError(ex.getMessage());
    }

    @ExceptionHandler(EmaiInvalido.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleEmaiInvalidoException(EmaiInvalido ex) {
        return new ApiError(ex.getMessage());
    }

    @ExceptionHandler(SenhaCurta.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleSenhaCurtaException(EmailJaCadastrado ex) {
        return new ApiError(ex.getMessage());
    }

    @ExceptionHandler(UsuarioNaoEncontrado.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handleUsuarioNaoEncontradoException(UsuarioNaoEncontrado ex) {
        return new ApiError(ex.getMessage());
    }
}
