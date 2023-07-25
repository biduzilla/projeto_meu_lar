package com.ricky.meu_lar.exception;

public class SenhaInvalida extends RuntimeException{
    public SenhaInvalida(){
        super("Senha Inválida");
    }
}
