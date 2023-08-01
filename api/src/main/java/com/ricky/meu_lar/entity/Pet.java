package com.ricky.meu_lar.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.ricky.meu_lar.entity.ENUM.StatusPet;
import com.ricky.meu_lar.entity.ENUM.TamanhoPet;
import lombok.*;

import javax.persistence.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@Entity
@Table(name = "tab_pet")
public class Pet {
    @Id
    @Column(name = "pet_id")
    private String id;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "ref_usuario")
    private Usuario usuario;

    @Column(name = "pet_nome")
    private String nome;

    @Lob
    @Column(name = "pet_descricao")
    private String descricao;

    @Column(name = "pet_status")
    private StatusPet status;

    @Lob
    @Column(name = "pet_img")
    private String imagem;

    @Column(name = "pet_tamanho")
    private TamanhoPet tamanho;
}
