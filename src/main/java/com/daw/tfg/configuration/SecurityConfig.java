package com.daw.tfg.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.daw.tfg.service.CustomUserDetails;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final CustomUserDetails userDetailsService;

    public SecurityConfig(CustomUserDetails userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public PasswordEncoder passwdEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/register/**").permitAll()
                        .requestMatchers("/h2-console/**").permitAll()
                        .requestMatchers("/static/**", "/estilos/**" , "/fontawesome-free-7.1.0-web/**").permitAll()
                        .anyRequest().permitAll()
                    )
            .formLogin(form -> form
                .loginPage("/login")
                .defaultSuccessUrl("/", true)
                .permitAll()
            )
            .logout(logout -> logout.permitAll())
            .headers(headers -> headers
                .frameOptions(fo -> fo.disable())
            );

        return http.build();
    }
}
