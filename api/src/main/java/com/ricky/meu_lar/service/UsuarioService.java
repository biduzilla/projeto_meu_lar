package com.ricky.meu_lar.service;

import com.ricky.meu_lar.dto.CredencialDto;
import com.ricky.meu_lar.dto.TokenDto;
import com.ricky.meu_lar.dto.UsuarioDto;

public interface UsuarioService {
    void salvarUser(UsuarioDto usuario);
    UsuarioDto getUserById(String id);
    void atualizarUser(UsuarioDto usuarioDto);
    void deletarUser(String id);
    TokenDto login(CredencialDto credencialDto);
}
