package com.ricky.meu_lar.repository;

import com.ricky.meu_lar.model.Pet;
import com.ricky.meu_lar.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PetRepository extends JpaRepository<Pet, String> {

}
