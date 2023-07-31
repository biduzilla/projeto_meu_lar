package com.ricky.meu_lar.controller;

import antlr.Token;
import com.ricky.meu_lar.dto.CredencialDto;
import com.ricky.meu_lar.dto.TokenDto;
import com.ricky.meu_lar.dto.UsuarioDto;
import com.ricky.meu_lar.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("api/usuario/")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;

    @PostMapping("criar")
    @ResponseStatus(HttpStatus.CREATED)
    public void salvarUsuario(@Valid @RequestBody UsuarioDto usuarioDto){
        usuarioService.salvarUser(usuarioDto);
    }

    @CrossOrigin
    @PostMapping("login")
    @ResponseStatus(HttpStatus.OK)
    public TokenDto login(@Valid @RequestBody CredencialDto credencialDto){
        return usuarioService.login(credencialDto);
    }

    @GetMapping("get_user/{id}")
    @ResponseStatus(HttpStatus.OK)
    public UsuarioDto getUsuarioById(@PathVariable String id){
        return usuarioService.getUserById(id);
    }

    @PutMapping("atualizar")
    @ResponseStatus(HttpStatus.OK)
    public void atualizarUsuario(@RequestBody @Valid UsuarioDto usuarioDto){
        usuarioService.atualizarUser(usuarioDto);
    }

    @DeleteMapping("deletar/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deletarUserById(@PathVariable String id){
        usuarioService.deletarUser(id);
    }

}
