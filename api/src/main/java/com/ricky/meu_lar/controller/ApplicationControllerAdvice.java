package com.ricky.meu_lar.controller;

import com.ricky.meu_lar.error.ApiError;
import com.ricky.meu_lar.exception.*;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.stream.Collectors;

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
    public ApiError handleSenhaCurtaException(SenhaCurta ex) {
        return new ApiError(ex.getMessage());
    }

    @ExceptionHandler(UsuarioNaoEncontrado.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handleUsuarioNaoEncontradoException(UsuarioNaoEncontrado ex) {
        return new ApiError(ex.getMessage());
    }

    @ExceptionHandler(SenhaInvalida.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleSenhaInvalidaException(SenhaInvalida ex) {
        return new ApiError(ex.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleMethodNotValidException(MethodArgumentNotValidException ex) {
        List<String> erros = ex.getBindingResult().getAllErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.toList());

        return new ApiError(erros);
    }
}
