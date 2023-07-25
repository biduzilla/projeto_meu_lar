package com.ricky.meu_lar.entity;

import lombok.*;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@Entity
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

    @Column(name = "usuario_admin")
    private boolean admin;

    @OneToMany(fetch=FetchType.LAZY,mappedBy = "usuario")
    private List<Pet> pets = new ArrayList<Pet>();
}
