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
public class PetRequisicaoDto {
    @NotEmpty(message = "{campo.nome.obrigatorio}")
    private String nome;
    @NotEmpty(message = "{campo.descricao.obrigatorio}")
    private String descricao;
    @NotEmpty(message = "{campo.status.obrigatorio}")
    private String status;
    @NotEmpty(message = "{campo.imagem.obrigatorio}")
    private String imagem;
    @NotEmpty(message = "{campo.tamanho.obrigatorio}")
    private String tamanho;
}
