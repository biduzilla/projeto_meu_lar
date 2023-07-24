package com.ricky.meu_lar.error;

import lombok.Data;

import java.util.List;

@Data
public class ApiError {
    private List<String> error;

    public ApiError(String mensagemError){
        this.error = List.of(mensagemError);
    }

    public ApiError(List<String> error){
        this.error = error;
    }
}
