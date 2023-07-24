package com.ricky.meu_lar.model;

import lombok.*;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Table(name = "tab_usuario")
public class Usuario {

    @Id
    @Column(name = "usuario_id")
    private String id = UUID.randomUUID().toString();

    @Column(name = "usuario_nome")
    private String nome;

    @Column(name = "usuario_email")
    private String email;

    @Column(name = "usuario_senha")
    private String senha;

    @Column(name = "usuario_telefone")
    private String telefone;

    @OneToMany(mappedBy = "usuario",cascade = CascadeType.ALL)
    private List<Pet> pets;
}
