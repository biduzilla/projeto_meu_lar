package com.ricky.meu_lar.repository;

import com.ricky.meu_lar.entity.ENUM.StatusPet;
import com.ricky.meu_lar.entity.ENUM.TamanhoPet;
import com.ricky.meu_lar.entity.Pet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PetRepository extends JpaRepository<Pet, String> {
    Optional<List<Pet>> findByStatus(StatusPet status);
    Optional<List<Pet>> findByTamanho(TamanhoPet tamanhoPet);

}
