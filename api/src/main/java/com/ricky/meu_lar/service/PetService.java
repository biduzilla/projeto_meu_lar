package com.ricky.meu_lar.service;

import com.ricky.meu_lar.dto.ListPetsDto;
import com.ricky.meu_lar.dto.PetRequisicaoDto;
import com.ricky.meu_lar.dto.PetResponseDto;
import com.ricky.meu_lar.dto.PetUpdateRequisicaoDto;

import java.util.List;

public interface PetService {
    void cadastrarPet(PetRequisicaoDto petDto, String idUser);

    void updatePet(PetUpdateRequisicaoDto petDto, String idPet, String idUser);

    void deletePet(String petId);

    void deleteMyPet(String petId, String idUser);

    PetResponseDto getPetById(String id);

    ListPetsDto getAllMyPets(String userId);

    ListPetsDto getAllPets();
    ListPetsDto getAllPetsPerdidosEncontrados();

    ListPetsDto getAllPetsSmall();

    ListPetsDto getAllPetsMedium();

    ListPetsDto getAllPetsLarge();

    ListPetsDto getAllPetsAdotar();

    ListPetsDto getAllPetsPerdido();

    ListPetsDto getAllPetsEncontrado();
}
