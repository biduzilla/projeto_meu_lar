package com.ricky.meu_lar.dto;

import lombok.*;

import javax.validation.constraints.NotEmpty;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class TokenDto {
    private String token;
    private String idUser;
}
