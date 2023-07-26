package com.ricky.meu_lar.controller;

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
    public List<PetResponseDto> getAllPets() {
        return petService.getAllPets();
    }

    @GetMapping("todos_pets_pequeno")
    public List<PetResponseDto> getAllPetsSmall() {
        return petService.getAllPetsSmall();
    }

    @GetMapping("todos_pets_medio")
    public List<PetResponseDto> getAllPetsMedium() {
        return petService.getAllPetsMedium();
    }

    @GetMapping("todos_pets_grande")
    public List<PetResponseDto> getAllPetsLarge() {
        return petService.getAllPetsLarge();
    }

    @GetMapping("todos_pets_adotar")
    public List<PetResponseDto> getAllPetsAdotar() {
        return petService.getAllPetsAdotar();
    }

    @GetMapping("todos_pets_encontrado")
    public List<PetResponseDto> getAllPetsEncontrado() {
        return petService.getAllPetsEncontrado();
    }

    @GetMapping("todos_pets_perdido")
    public List<PetResponseDto> getAllPetsPerdido() {
        return petService.getAllPetsPerdido();
    }

    @PostMapping("cadastrar_pet/{idUser}")
    public void cadastrarPet(@Valid @RequestBody PetRequisicaoDto pet, String idUser) {
        petService.cadastrarPet(pet, idUser);
    }

    @PutMapping("atualizar_pet/{idPet}/{idUser}")
    public void atualizarPet(@Valid @RequestBody PetUpdateRequisicaoDto pet, String idPet, String idUser) {
        petService.updatePet(pet, idPet, idUser);
    }

    @DeleteMapping("apagar_pet/{idPet}/{idUser}")
    public void apagarCadastroPet(@PathVariable String idPet, @PathVariable String idUser) {
        petService.deleteMyPet(idPet, idUser);
    }
}
