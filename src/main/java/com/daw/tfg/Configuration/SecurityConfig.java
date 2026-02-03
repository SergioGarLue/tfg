package com.daw.tfg.Configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwdEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        //creamos los filtros
        http.authorizeHttpRequests(auth -> auth
            .requestMatchers("/").permitAll() //publico
            .requestMatchers("/añadir/**").authenticated() //privado
            .requestMatchers("/registro").permitAll() //publico
            .anyRequest().permitAll() //cualquier ruta no registrada
        )
        .formLogin(Customizer.withDefaults())
        .logout(Customizer.withDefaults());
        
        return http.build(); // devolvemos los filtros que hemos creado
    }
}
