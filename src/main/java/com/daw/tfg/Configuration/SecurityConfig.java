package com.daw.tfg.Configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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

        // creamos los filtros
        http.authorizeHttpRequests(auth -> auth
            .requestMatchers("/").permitAll()   // público
            .requestMatchers("").hasRole("USER") // privado para usuarios con rol USER
            .requestMatchers("").hasRole("ADMIN")
            .anyRequest().authenticated()  // cualquier ruta no registrada tiene que ser autenticado
        )
        .formLogin(form ->form
            .loginPage("/login")
            .loginProcessingUrl("/login")
            .defaultSuccessUrl("/",true)
            .permitAll()
        )
        .logout(logout -> logout
            .logoutUrl("/logout")
            .logoutSuccessUrl("/?logout=true")
            .invalidateHttpSession(true)
            .clearAuthentication(true)
            .deleteCookies("")
            .permitAll()
        );

        return http.build(); // devolvemos los filtros que hemos creado
    }
}
