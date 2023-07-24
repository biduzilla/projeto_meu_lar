package com.ricky.meu_lar.dto;

import com.ricky.meu_lar.entity.Pet;
import lombok.*;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class UsuarioDto {
    private String id;
    @NotEmpty(message = "{campo.nome.obrigatorio}")
    private String nome;
    @NotEmpty(message = "{campo.email.obrigatorio}")
    private String email;
    @NotEmpty(message = "{campo.senha.obrigatorio}")
    private String senha;
    @NotEmpty(message = "{campo.telefone.obrigatorio}")
    private String telefone;
    private List<Pet> pets;
}
