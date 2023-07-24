package com.ricky.meu_lar.service.impl;

import com.ricky.meu_lar.dto.CredencialDto;
import com.ricky.meu_lar.dto.UsuarioDto;
import com.ricky.meu_lar.exception.EmaiInvalido;
import com.ricky.meu_lar.exception.EmailJaCadastrado;
import com.ricky.meu_lar.exception.SenhaCurta;
import com.ricky.meu_lar.exception.UsuarioNaoEncontrado;
import com.ricky.meu_lar.entity.Usuario;
import com.ricky.meu_lar.repository.UsuarioRepository;
import com.ricky.meu_lar.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;

    @Override
    public void salvarUser(UsuarioDto usuario) {
        if (usuarioRepository.existsByEmail(usuario.getEmail())) {
            throw new EmailJaCadastrado();
        }
        if (!validEmail(usuario.getEmail())) {
            throw new EmaiInvalido();
        }
        if (!validSenha(usuario.getSenha())) {
            throw new SenhaCurta();
        }
        usuarioRepository.save(Usuario.builder()
                .id(UUID.randomUUID().toString())
                .nome(usuario.getNome())
                .email(usuario.getEmail())
                .senha(usuario.getSenha())
                .telefone(usuario.getTelefone())
                .build());
    }

    @Override
    public UsuarioDto getUserById(String id) {

        Usuario usuario = usuarioRepository.findById(id).orElseThrow(UsuarioNaoEncontrado::new);

        return UsuarioDto.builder()
                .id(usuario.getId())
                .email(usuario.getEmail())
                .senha(usuario.getSenha())
                .telefone(usuario.getTelefone())
                .pets(usuario.getPets())
                .build();
    }

    @Override
    public void atualizarUser(UsuarioDto usuarioDto) {
        if (usuarioRepository.existsById(usuarioDto.getId())) {
            if (usuarioRepository.existsByEmail(usuarioDto.getEmail())) {
                throw new EmailJaCadastrado();
            }
            if (!validEmail(usuarioDto.getEmail())) {
                throw new EmaiInvalido();
            }
            if (!validSenha(usuarioDto.getSenha())) {
                throw new SenhaCurta();
            }
            usuarioRepository.save(Usuario.builder()
                    .id(usuarioDto.getId())
                    .email(usuarioDto.getEmail())
                    .senha(usuarioDto.getSenha())
                    .telefone(usuarioDto.getTelefone())
                    .pets(usuarioDto.getPets())
                    .build());
        } else {
            throw new UsuarioNaoEncontrado();
        }
    }

    @Override
    public void deletarUser(String id) {
        Usuario usuario = usuarioRepository.findById(id).orElseThrow(UsuarioNaoEncontrado::new);

        usuarioRepository.delete(usuario);
    }

    @Override
    public UsuarioDto login(CredencialDto credencialDto) {
        Usuario usuario = usuarioRepository.findByEmailAndSenha(credencialDto.getEmail(), credencialDto.getSenha()).orElseThrow(UsuarioNaoEncontrado::new);
        return UsuarioDto.builder()
                .id(usuario.getId())
                .build();
    }

    private boolean validEmail(String email) {
        String regexPattern = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
                + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
        Pattern pattern = Pattern.compile(regexPattern, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    private boolean validSenha(String senha) {
        return senha.toCharArray().length > 8;
    }
}
