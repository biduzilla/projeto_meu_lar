package com.ricky.meu_lar.exception;

public class SenhaCurta extends RuntimeException{
    public SenhaCurta(){
        super("Senha deve ter mais que 8 dígitos");
    }
}
