package com.ricky.meu_lar.repository;

import com.ricky.meu_lar.entity.Pet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PetRepository extends JpaRepository<Pet, String> {

}
