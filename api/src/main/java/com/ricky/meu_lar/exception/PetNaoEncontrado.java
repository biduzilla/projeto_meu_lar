package com.ricky.meu_lar.exception;

public class PetNaoEncontrado extends RuntimeException{
    public PetNaoEncontrado(){
        super("Pet não encontrado");
    }
}
