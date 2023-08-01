package com.ricky.meu_lar.service.impl;

import com.ricky.meu_lar.dto.CredencialDto;
import com.ricky.meu_lar.dto.PetResponseDto;
import com.ricky.meu_lar.dto.TokenDto;
import com.ricky.meu_lar.dto.UsuarioDto;
import com.ricky.meu_lar.entity.Pet;
import com.ricky.meu_lar.exception.EmaiInvalido;
import com.ricky.meu_lar.exception.EmailJaCadastrado;
import com.ricky.meu_lar.exception.SenhaCurta;
import com.ricky.meu_lar.exception.UsuarioNaoEncontrado;
import com.ricky.meu_lar.entity.Usuario;
import com.ricky.meu_lar.repository.UsuarioRepository;
import com.ricky.meu_lar.security.JwtService;
import com.ricky.meu_lar.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioServiceAuthImpl usuarioServiceAuth;
    private final JwtService jwtService;
    private final PasswordEncoder encoder;

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
                .senha(encoder.encode(usuario.getSenha()))
                .telefone(usuario.getTelefone())
                .admin(false)
                .build());
    }

    @Override
    public UsuarioDto getUserById(String id) {

        Usuario usuario = usuarioRepository.findById(id).orElseThrow(UsuarioNaoEncontrado::new);

        return UsuarioDto.builder()
                .id(usuario.getId())
                .nome(usuario.getNome())
                .email(usuario.getEmail())
                .telefone(usuario.getTelefone())
                .pets(listPetToListPetDto(usuario.getPets()))
                .build();
    }

    private List<PetResponseDto> listPetToListPetDto(List<Pet> pets) {
        return pets.stream()
                .map(pet -> PetResponseDto.builder()
                        .id(pet.getId())
                        .nomePet(pet.getNome())
                        .nomeContato(pet.getUsuario().getNome())
                        .descricao(pet.getDescricao())
                        .status(pet.getStatus().name())
                        .imagem(pet.getImagem())
                        .tamanho(pet.getTamanho().name())
                        .telefone(pet.getUsuario().getTelefone())
                        .build()).collect(Collectors.toList());
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

            Usuario usuario = usuarioRepository.findById(usuarioDto.getId())
                    .orElseThrow(UsuarioNaoEncontrado::new);

            if (!usuarioDto.getEmail().isEmpty() || usuarioDto.getEmail() != null) {
                usuario.setEmail(usuarioDto.getEmail());
            }
            if (!usuarioDto.getNome().isEmpty() || usuarioDto.getNome() != null) {
                usuario.setNome(usuarioDto.getNome());
            }
            if (!usuarioDto.getTelefone().isEmpty() || usuarioDto.getTelefone() != null) {
                usuario.setTelefone(usuarioDto.getTelefone());
            }
            if (!usuarioDto.getSenha().isEmpty() || usuarioDto.getSenha() != null) {
                usuario.setSenha(usuarioDto.getSenha());
            }

            usuarioRepository.save(usuario);

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
    public TokenDto login(CredencialDto credencialDto) {
        try {
            Usuario usuario =
                    Usuario.builder()
                            .email(credencialDto.getEmail())
                            .senha(credencialDto.getSenha())
                            .build();

            UserDetails userAutentificado = usuarioServiceAuth.autentificar(usuario);
            String token = jwtService.gerarToken(usuario);

            Usuario userPronto = usuarioRepository.findByEmail(usuario.getEmail()).orElseThrow(UsuarioNaoEncontrado::new);

            return new TokenDto(token, userPronto.getId(), userPronto.getNome());
        } catch (UsuarioNaoEncontrado e) {
            throw new UsuarioNaoEncontrado();
        }
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
