package com.ricky.meu_lar.exception;

public class UsuarioNaoEncontrado extends RuntimeException{
    public UsuarioNaoEncontrado(){
        super("Usuario não encontrado");
    }
}
