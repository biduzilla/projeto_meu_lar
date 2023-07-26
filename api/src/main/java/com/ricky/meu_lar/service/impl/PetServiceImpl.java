package com.ricky.meu_lar.service.impl;

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
                .id(UUID.randomUUID().toString())
                .usuario(usuario)
                .nome(petDto.getNome())
                .descricao(petDto.getDescricao())
                .status(petDto.getStatus())
                .imagem(petDto.getImagem())
                .tamanho(petDto.getTamanho())
                .build();

        petRepository.save(pet);
    }

    @Override
    public void updatePet(PetUpdateRequisicaoDto petDto, String idPet, String idUser) {
        Usuario usuario = usuarioRepository.findById(idUser).orElseThrow(UsuarioNaoEncontrado::new);

        if (verificarSeContemId(usuario.getPets(), idPet)) {
            Pet pet = petRepository.findById(idPet).orElseThrow(PetNaoEncontrado::new);

            pet.setNome(petDto.getNome());
            pet.setDescricao(petDto.getDescricao());
            pet.setStatus(petDto.getStatus());
            pet.setTamanho(petDto.getTamanho());
            pet.setImagem(petDto.getImagem());

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
                .status(pet.getStatus())
                .imagem(pet.getImagem())
                .tamanho(pet.getTamanho())
                .telefone(usuario.getTelefone())
                .build();

    }

    @Override
    public List<PetResponseDto> getAllMyPets(String userId) {
        Usuario usuario = usuarioRepository.findById(userId).orElseThrow(UsuarioNaoEncontrado::new);

        return listPetToListPetDto(usuario.getPets());
    }

    private List<PetResponseDto> listPetToListPetDto(List<Pet> pets) {
        return pets.stream()
                .map(pet -> PetResponseDto.builder()
                        .id(pet.getId())
                        .nomePet(pet.getNome())
                        .nomeContato(pet.getUsuario().getNome())
                        .descricao(pet.getDescricao())
                        .status(pet.getStatus())
                        .imagem(pet.getImagem())
                        .tamanho(pet.getTamanho())
                        .telefone(pet.getUsuario().getTelefone())
                        .build()).collect(Collectors.toList());
    }

    @Override
    public List<PetResponseDto> getAllPets() {
        List<Pet> pets = petRepository.findAll();

        return listPetToListPetDto(pets);
    }

    @Override
    public List<PetResponseDto> getAllPetsSmall() {
        List<Pet> pets = petRepository.findByTamanho(TamanhoPet.PEQUENO);

        return listPetToListPetDto(pets);
    }

    @Override
    public List<PetResponseDto> getAllPetsMedium() {
        List<Pet> pets = petRepository.findByTamanho(TamanhoPet.MEDIO);

        return listPetToListPetDto(pets);
    }

    @Override
    public List<PetResponseDto> getAllPetsLarge() {
        List<Pet> pets = petRepository.findByTamanho(TamanhoPet.GRANDE);

        return listPetToListPetDto(pets);
    }

    @Override
    public List<PetResponseDto> getAllPetsAdotar() {
        List<Pet> pets = petRepository.findByStatus(StatusPet.ADOTAR);

        return listPetToListPetDto(pets);
    }

    @Override
    public List<PetResponseDto> getAllPetsPerdido() {
        List<Pet> pets = petRepository.findByStatus(StatusPet.PERDIDO);

        return listPetToListPetDto(pets);
    }

    @Override
    public List<PetResponseDto> getAllPetsEncontrado() {
        List<Pet> pets = petRepository.findByStatus(StatusPet.ENCONTRADO);

        return listPetToListPetDto(pets);
    }
}
