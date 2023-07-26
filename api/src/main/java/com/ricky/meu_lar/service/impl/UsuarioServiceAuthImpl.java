package com.ricky.meu_lar.service.impl;

import com.ricky.meu_lar.entity.Usuario;
import com.ricky.meu_lar.exception.SenhaInvalida;
import com.ricky.meu_lar.exception.UsuarioNaoEncontrado;
import com.ricky.meu_lar.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UsuarioServiceAuthImpl implements UserDetailsService {

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByEmail(username)
                .orElseThrow(UsuarioNaoEncontrado::new);

        String[] roles = usuario.isAdmin() ?
                new String[]{"ADMIN", "USER"} : new String[]{"USER"};
        return User
                .builder()
                .username(usuario.getEmail())
                .password(usuario.getSenha())
                .roles(roles)
                .build();
    }

    public UserDetails autentificar(Usuario usuario) {
        UserDetails userDetails = loadUserByUsername(usuario.getEmail());
        boolean senhasBatem = encoder.matches(usuario.getSenha(), userDetails.getPassword());

        if (senhasBatem) {
            return userDetails;
        }
        throw new SenhaInvalida();
    }

    public Usuario procurarUsuarioPorToken(String token) {
        if (token.contains("Bearer")) {
            token = token.split(" ")[1];
        }
        return null;
    }
}
