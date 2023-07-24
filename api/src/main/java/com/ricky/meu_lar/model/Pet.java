package com.ricky.meu_lar.model;

import com.ricky.meu_lar.model.ENUM.StatusPet;
import lombok.*;

import org.springframework.data.annotation.Id;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.UUID;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@Table(name = "tab_pet")
public class Pet {
    @Id
    @Column(name = "pet_id")
    private String id = UUID.randomUUID().toString();

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario user;

    @Column(name = "pet_nome")
    private String nome;

    @Column(name = "pet_descricao")
    private String descricao;

    @Column(name = "pet_status")
    private StatusPet status;

    @Column(name = "pet_img")
    private String imagem;
}
