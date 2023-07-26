package com.ricky.meu_lar.repository;

import com.ricky.meu_lar.entity.ENUM.StatusPet;
import com.ricky.meu_lar.entity.ENUM.TamanhoPet;
import com.ricky.meu_lar.entity.Pet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PetRepository extends JpaRepository<Pet, String> {
    List<Pet> findByStatus(StatusPet status);
    List<Pet> findByTamanho(TamanhoPet tamanhoPet);

}
