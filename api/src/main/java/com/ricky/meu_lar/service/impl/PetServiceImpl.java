package com.ricky.meu_lar.service.impl;

import com.ricky.meu_lar.dto.ListPetsDto;
import com.ricky.meu_lar.dto.PetRequisicaoDto;
import com.ricky.meu_lar.dto.PetResponseDto;
import com.ricky.meu_lar.dto.PetUpdateRequisicaoDto;
import com.ricky.meu_lar.entity.ENUM.StatusPet;
import com.ricky.meu_lar.entity.ENUM.TamanhoPet;
import com.ricky.meu_lar.entity.Pet;
import com.ricky.meu_lar.entity.Usuario;
import com.ricky.meu_lar.exception.PetNaoEncontrado;
import com.ricky.meu_lar.exception.PetNaoTePertence;
import com.ricky.meu_lar.exception.UsuarioNaoEncontrado;
import com.ricky.meu_lar.repository.PetRepository;
import com.ricky.meu_lar.repository.UsuarioRepository;
import com.ricky.meu_lar.service.PetService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PetServiceImpl implements PetService {
    private final UsuarioRepository usuarioRepository;
    private final PetRepository petRepository;

    @Override
    public void cadastrarPet(PetRequisicaoDto petDto, String idUser) {
        Usuario usuario = usuarioRepository.findById(idUser).orElseThrow(UsuarioNaoEncontrado::new);

        Pet pet = Pet.builder()
                .id(petDto.getId())
                .usuario(usuario)
                .nome(petDto.getNome())
                .descricao(petDto.getDescricao())
                .status(StatusPet.valueOf(petDto.getStatus()))
                .imagem(petDto.getImagem())
                .tamanho(TamanhoPet.valueOf(petDto.getTamanho()))
                .build();

        petRepository.save(pet);
    }

    @Override
    public void updatePet(PetUpdateRequisicaoDto petDto, String idPet, String idUser) {
        Usuario usuario = usuarioRepository.findById(idUser).orElseThrow(UsuarioNaoEncontrado::new);

        if (verificarSeContemId(usuario.getPets(), idPet)) {
            Pet pet = petRepository.findById(idPet).orElseThrow(PetNaoEncontrado::new);

            if (!petDto.getNome().isEmpty() || petDto.getNome() != null) {
                pet.setNome(petDto.getNome());
            }
            if (!petDto.getDescricao().isEmpty() || petDto.getDescricao() != null) {
                pet.setDescricao(petDto.getDescricao());
            }
            if (!petDto.getStatus().isEmpty() || petDto.getStatus() != null) {
                pet.setStatus(StatusPet.valueOf(petDto.getStatus()));
            }
            if (!petDto.getTamanho().isEmpty() || petDto.getTamanho() != null) {
                pet.setTamanho(TamanhoPet.valueOf(petDto.getTamanho()));
            }
            if (!petDto.getImagem().isEmpty() || petDto.getImagem() != null) {
                pet.setImagem(petDto.getImagem());
            }

            petRepository.save(pet);
        } else {
            throw new PetNaoTePertence();
        }
    }

    private boolean verificarSeContemId(List<Pet> pets, String idPet) {
        for (Pet pet : pets) {
            if (pet.getId().equals(idPet)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void deletePet(String idPet) {
        try {
            petRepository.deleteById(idPet);
        } catch (Exception e) {
            throw new PetNaoEncontrado();
        }
    }

    @Override
    public void deleteMyPet(String idPet, String idUser) {
        Usuario usuario = usuarioRepository.findById(idUser).orElseThrow(UsuarioNaoEncontrado::new);

        if (verificarSeContemId(usuario.getPets(), idPet)) {
            try {
                petRepository.deleteById(idPet);
            } catch (Exception e) {
                throw new PetNaoEncontrado();
            }
        } else {
            throw new PetNaoTePertence();
        }
    }

    @Override
    public PetResponseDto getPetById(String petId) {
        Pet pet = petRepository.findById(petId).orElseThrow(PetNaoEncontrado::new);
        return petToPetResponse(pet);
    }

    private PetResponseDto petToPetResponse(Pet pet) {
        Usuario usuario = usuarioRepository.findById(pet
                        .getUsuario()
                        .getId())
                .orElseThrow(UsuarioNaoEncontrado::new);

        return PetResponseDto.builder()
                .id(pet.getId())
                .nomePet(pet.getNome())
                .nomeContato(usuario.getNome())
                .descricao(pet.getDescricao())
                .status(pet.getStatus().name())
                .imagem(pet.getImagem())
                .tamanho(pet.getTamanho().name())
                .telefone(usuario.getTelefone())
                .build();

    }

    @Override
    public ListPetsDto getAllMyPets(String userId) {
        Usuario usuario = usuarioRepository.findById(userId).orElseThrow(UsuarioNaoEncontrado::new);

        return listPetToListPetDto(usuario.getPets());
    }

    private ListPetsDto listPetToListPetDto(List<Pet> pets) {
        return new ListPetsDto(
                pets.stream().map(pet -> {
                    Usuario usuario = pet.getUsuario();
                    return new PetResponseDto(
                            pet.getId(),
                            pet.getNome(),
                            usuario.getNome(),
                            pet.getDescricao(),
                            pet.getStatus().name(),
                            pet.getImagem(),
                            pet.getTamanho().name(),
                            pet.getUsuario().getTelefone()
                    );
                }).collect(Collectors.toList())
        );
    }

    @Override
    public ListPetsDto getAllPets() {
        List<Pet> pets = petRepository.findAll();

        return listPetToListPetDto(pets);
    }

    @Override
    public ListPetsDto getAllPetsPerdidosEncontrados() {


        List<Pet> pets = petRepository.findByStatus(StatusPet.ENCONTRADO).orElseThrow(PetNaoEncontrado::new);
        pets.addAll(petRepository.findByStatus(StatusPet.PERDIDO).orElseThrow(PetNaoEncontrado::new));

        return listPetToListPetDto(pets);
    }

    @Override
    public ListPetsDto getAllPetsSmall() {
        List<Pet> pets = petRepository.findByTamanho(TamanhoPet.PEQUENO).orElseThrow(PetNaoEncontrado::new);

        return listPetToListPetDto(pets);
    }

    @Override
    public ListPetsDto getAllPetsMedium() {
        List<Pet> pets = petRepository.findByTamanho(TamanhoPet.MEDIO).orElseThrow(PetNaoEncontrado::new);

        return listPetToListPetDto(pets);
    }

    @Override
    public ListPetsDto getAllPetsLarge() {
        List<Pet> pets = petRepository.findByTamanho(TamanhoPet.GRANDE).orElseThrow(PetNaoEncontrado::new);

        return listPetToListPetDto(pets);
    }

    @Override
    public ListPetsDto getAllPetsAdotar() {
        List<Pet> pets = petRepository.findByStatus(StatusPet.ADOTAR).orElseThrow(PetNaoEncontrado::new);

        return listPetToListPetDto(pets);
    }

    @Override
    public ListPetsDto getAllPetsPerdido() {
        List<Pet> pets = petRepository.findByStatus(StatusPet.PERDIDO).orElseThrow(PetNaoEncontrado::new);

        return listPetToListPetDto(pets);
    }

    @Override
    public ListPetsDto getAllPetsEncontrado() {
        List<Pet> pets = petRepository.findByStatus(StatusPet.ENCONTRADO).orElseThrow(PetNaoEncontrado::new);

        return listPetToListPetDto(pets);
    }
}
