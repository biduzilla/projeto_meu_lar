package com.ricky.meu_lar.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import com.ricky.meu_lar.entity.Usuario;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Service
public class JwtService {

    @Value("${security.jwt.expiracao}")
    private String expiracao;

    @Value("${security.jwt.key}")
    private String key;

    public String gerarToken(Usuario usuario) throws ExpiredJwtException {
        long expString = Long.parseLong(expiracao);
        LocalDateTime dataHoraExpiracao = LocalDateTime.now().plusMinutes(expString);
        Instant instant = dataHoraExpiracao.atZone(ZoneId.systemDefault()).toInstant();
        Date date = Date.from(instant);

        return Jwts.builder()
                .setSubject(usuario.getEmail())
                .setExpiration(date)
                .signWith(SignatureAlgorithm.ES512, key)
                .compact();
    }

    private Claims obterClaims(String token) {
        return Jwts
                .parser()
                .setSigningKey(key)
                .parseClaimsJws(token)
                .getBody();
    }

    public boolean validaToken(String token) {
        try {
            Claims claims = obterClaims(token);
            Date dataExpiracao = claims.getExpiration();
            LocalDateTime data = dataExpiracao
                    .toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDateTime();
            return !LocalDateTime.now().isAfter(data);

        } catch (Exception e) {
            return false;
        }
    }

    public String obterLoginUser(String token) throws ExpiredJwtException {
        return (String) obterClaims(token).getSubject();
    }
}
