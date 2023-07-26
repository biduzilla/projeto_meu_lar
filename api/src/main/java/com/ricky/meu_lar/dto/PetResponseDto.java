package com.ricky.meu_lar.dto;

import com.ricky.meu_lar.entity.ENUM.StatusPet;
import com.ricky.meu_lar.entity.ENUM.TamanhoPet;
import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class PetResponseDto {
    private String id;
    private String nomePet;
    private String nomeContato;
    private String descricao;
    private StatusPet status;
    private String imagem;
    private TamanhoPet tamanho;
    private String telefone;
}
