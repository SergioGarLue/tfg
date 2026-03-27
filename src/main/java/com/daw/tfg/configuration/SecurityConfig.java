package com.daw.tfg.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwdEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        // creamos los filtros
        // problema con los DELETE,PUT Y POST por el csrf deshabilidato para pruebas
        // posteriormente se deberia cambiar para que compruebe si tienes un JWT(token)
        // y en caso de tenerlo permitir estos metodos curl (por seguridad)
        http
                .csrf(
                    csrf -> csrf.disable()
                ) // esta linea .csrf
                .headers(headers -> headers
                        .frameOptions(frame -> frame.sameOrigin())
                    )
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/**").permitAll() // público
                        .anyRequest().permitAll() // cualquier ruta no registrada tiene que ser autenticado
                )

                .formLogin(form -> form
                        .loginPage("/login")
                        .loginProcessingUrl("/login")
                        .defaultSuccessUrl("/", true)
                        .permitAll())
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/?logout=true")
                        .invalidateHttpSession(true)
                        .clearAuthentication(true)
                        .permitAll());

        return http.build(); // devolvemos los filtros que hemos creado
    }
}
