package com.ricky.meu_lar.dto;

import lombok.*;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class ListPetsDto {
    List<PetResponseDto> pets;
}
