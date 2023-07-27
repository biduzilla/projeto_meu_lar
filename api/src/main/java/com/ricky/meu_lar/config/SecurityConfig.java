package com.ricky.meu_lar.config;

import com.ricky.meu_lar.security.JwtAuthFilter;
import com.ricky.meu_lar.security.JwtService;
import com.ricky.meu_lar.service.impl.UsuarioServiceAuthImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.OncePerRequestFilter;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UsuarioServiceAuthImpl usuarioService;
    @Autowired
    private JwtService jwtService;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(usuarioService)
                .passwordEncoder(passwordEncoder());
    }

    @Bean
    public OncePerRequestFilter jwtFilter() {
        return new JwtAuthFilter(jwtService, usuarioService);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/api/pet/**")
                .hasAnyRole("USER", "ADMIN")
                .antMatchers("/api/usuario/criar")
                .permitAll()
                .antMatchers("/api/usuario/login")
                .permitAll()
                .antMatchers("/api/usuario/atualizar")
                .hasAnyRole("USER", "ADMIN")
                .antMatchers("/api/usuario/deletar/**")
                .hasRole("ADMIN")
                .antMatchers("/api/usuario/get_user/**")
                .hasAnyRole("USER", "ADMIN")
                .antMatchers("/h2-console/**")
                .permitAll()
                .antMatchers("/h2-console/")
                .permitAll()
                .antMatchers("/h2-console")
                .permitAll()
                .anyRequest().authenticated()
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .addFilterBefore(jwtFilter(), UsernamePasswordAuthenticationFilter.class);

        http.headers().frameOptions().disable();
    }
}
