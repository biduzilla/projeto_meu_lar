package com.ricky.meu_lar.dto;

import lombok.*;

import javax.validation.constraints.NotEmpty;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class CredencialDto {
    @NotEmpty(message = "{campo.email.obrigatorio}")
    private String email;
    @NotEmpty(message = "{campo.senha.obrigatorio}")
    private String senha;
}
