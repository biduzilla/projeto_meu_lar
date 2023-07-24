package com.ricky.meu_lar.exception;

public class EmaiInvalido extends RuntimeException{
    public EmaiInvalido(){
        super("Email Inválido");
    }
}
