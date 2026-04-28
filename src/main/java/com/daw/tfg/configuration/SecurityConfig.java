package com.daw.tfg.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.daw.tfg.security.JwtAuthenticationFilter;

@Configuration
@EnableMethodSecurity
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
                http
                                .csrf(csrf -> csrf.disable())
                                .sessionManagement(session -> session
                                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                                .headers(headers -> headers
                                                .frameOptions(fo -> fo.disable()))
                                .authorizeHttpRequests(auth -> auth
                                                // Rutas públicas de vistas
                                                .requestMatchers(
                                                                "/", "/index.html",
                                                                "/login", "/login.html",
                                                                "/registro", "/registro.html",
                                                                "/tienda", "/tienda/**", "/juego/**",
                                                                "/perfil", "/coleccion", "/deseados",
                                                                "/carrito", "/amigos", "/configuracion",
                                                                "/desarrollador/**", "/admin/**",
                                                                "/success", "/success/**", "/success.html",
                                                                "/pago-exitoso", "/pago-exitoso/**")
                                                .permitAll()
                                                // Endpoints de autenticación
                                                .requestMatchers("/api/auth/**").permitAll()
                                                // Recursos estáticos
                                                .requestMatchers("/static/**", "/sidebar.html", "/stripe-payment.html",
                                                                "/js/**", "/css/**", "/estilos/**",
                                                                "/fontawesome-free-7.1.0-web/**", "/images/**",
                                                                "/JSON/**", "/favicon.ico")
                                                .permitAll()
                                                // Tienda pública
                                                .requestMatchers("/api/tienda/**").permitAll()
                                                // H2-Console
                                                .requestMatchers("/h2-console/**").permitAll()
                                                // Admin endpoints
                                                .requestMatchers("/admin/**", "/api/admin/**").hasRole("ADMIN")
                                                // Webhook de Stripe (debe ir ANTES de /api/**)
                                                .requestMatchers("/api/stripe/webhook").permitAll()
                                                // APIs por defecto requieren autenticación
                                                .requestMatchers("/api/**").authenticated()
                                                // Cualquier otra petición requiere autenticación
                                                .anyRequest().authenticated())
                                .exceptionHandling(exception -> exception
                                                .authenticationEntryPoint(
                                                                new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)))
                                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

                return http.build(); // devolvemos los filtros que hemos creado
        }
}
