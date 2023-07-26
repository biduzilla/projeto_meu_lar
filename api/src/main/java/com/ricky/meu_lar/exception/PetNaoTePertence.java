package com.ricky.meu_lar.exception;

public class PetNaoTePertence extends RuntimeException{
    public PetNaoTePertence(){
        super("Você não é o dono desse pet");
    }
}
