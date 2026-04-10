package com.daw.tfg.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
// import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import com.daw.tfg.security.JwtAuthenticationFilter;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthFilter) {
        this.jwtAuthFilter = jwtAuthFilter;
    }

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
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .headers(headers -> headers
                        .frameOptions(fo -> fo.disable())
                )
.authorizeHttpRequests(auth -> auth
                        // Permitimos todos los endpoints de autenticación (/login, /register, /refresh)
                        // .requestMatchers("/api/auth/**").permitAll()
                        // // Permitimos la raíz y las páginas HTML principales
                        // .requestMatchers("/", "/index.html", "/formulario.html", "/login.html", "/h2-console/**").permitAll()
                        // // Permitimos recursos estáticos (JS, CSS, Imágenes) sin importar la carpeta
                        // .requestMatchers("/*.js", "/*.css", "/js/**", "/css/**", "/static/**", "/estilos/**", "/favicon.ico").permitAll()
                        // .requestMatchers("/admin/**").hasRole("ADMIN")
                        .anyRequest().permitAll())
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build(); // devolvemos los filtros que hemos creado
    }
}
