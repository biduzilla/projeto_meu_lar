package com.ricky.meu_lar.controller;

import com.ricky.meu_lar.dto.ListPetsDto;
import com.ricky.meu_lar.dto.PetRequisicaoDto;
import com.ricky.meu_lar.dto.PetResponseDto;
import com.ricky.meu_lar.dto.PetUpdateRequisicaoDto;
import com.ricky.meu_lar.service.PetService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("api/pet/")
@RequiredArgsConstructor
public class PetController {

    private final PetService petService;

    @GetMapping("dados_pet/{id}")
    public PetResponseDto getDadosPet(@PathVariable String id) {
        return petService.getPetById(id);
    }

    @GetMapping("todos_pets")
    public ListPetsDto getAllPets() {
        return petService.getAllPets();
    }

    @GetMapping("todos_pets_pequeno")
    public ListPetsDto getAllPetsSmall() {
        return petService.getAllPetsSmall();
    }

    @GetMapping("todos_pets_medio")
    public ListPetsDto getAllPetsMedium() {
        return petService.getAllPetsMedium();
    }

    @GetMapping("todos_pets_grande")
    public ListPetsDto getAllPetsLarge() {
        return petService.getAllPetsLarge();
    }

    @GetMapping("todos_pets_adotar")
    public ListPetsDto getAllPetsAdotar() {
        return petService.getAllPetsAdotar();
    }

    @GetMapping("todos_pets_encontrado")
    public ListPetsDto getAllPetsEncontrado() {
        return petService.getAllPetsEncontrado();
    }

    @GetMapping("todos_pets_perdido")
    public ListPetsDto getAllPetsPerdido() {
        return petService.getAllPetsPerdido();
    }

    @PostMapping("cadastrar_pet/{idUser}")
    public void cadastrarPet(@Valid @RequestBody PetRequisicaoDto pet,@PathVariable String idUser) {
        petService.cadastrarPet(pet, idUser);
    }

    @PutMapping("atualizar_pet/{idPet}/{idUser}")
    public void atualizarPet(@Valid @RequestBody PetUpdateRequisicaoDto pet,@PathVariable String idPet,@PathVariable String idUser) {
        petService.updatePet(pet, idPet, idUser);
    }

    @DeleteMapping("apagar_pet/{idPet}/{idUser}")
    public void apagarCadastroPet(@PathVariable String idPet, @PathVariable String idUser) {
        petService.deleteMyPet(idPet, idUser);
    }
}
