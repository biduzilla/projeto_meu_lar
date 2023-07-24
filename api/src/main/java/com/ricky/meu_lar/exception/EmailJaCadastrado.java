package com.ricky.meu_lar.exception;

public class EmailJaCadastrado extends RuntimeException{
    public EmailJaCadastrado(){
        super("Email já cadastrado");
    }
}
