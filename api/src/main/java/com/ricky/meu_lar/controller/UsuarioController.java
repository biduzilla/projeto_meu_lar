package com.ricky.meu_lar.controller;

import com.ricky.meu_lar.dto.UsuarioDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("api/usuario/")
@RequiredArgsConstructor
public class UsuarioController {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void salvarUsuario(@RequestBody @Valid UsuarioDto usuario){

    }
}
