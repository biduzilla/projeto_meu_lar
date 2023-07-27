package com.ricky.meu_lar.dto;

import com.ricky.meu_lar.entity.ENUM.StatusPet;
import com.ricky.meu_lar.entity.ENUM.TamanhoPet;
import lombok.*;

import javax.validation.constraints.NotEmpty;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class PetUpdateRequisicaoDto {
    private String nome;
    private String descricao;
    private String status;
    private String imagem;
    private String tamanho;
}
