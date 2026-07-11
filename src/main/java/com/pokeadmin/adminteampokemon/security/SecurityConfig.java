package com.pokeadmin.adminteampokemon.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

        private final JwtAuthenticationFilter jwtAuthenticationFilter;

        public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
                this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        }

        @Bean
        public SecurityFilterChain securityFilterChain(
                        HttpSecurity http) throws Exception {

                return http.csrf(csrf -> csrf.disable())
                                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                                .authorizeHttpRequests(auth -> auth
                                        .requestMatchers(
                                        "/api/v1/auth/**",
                                        "/swagger-ui/**",
                                        "/v3/api-docs/**",
                                        "/api/pokedex/**",
                                        "/actuator/health",
                                        "/actuator/info"
                                        ).permitAll()
                                        .anyRequest().authenticated())
                                .addFilterBefore(
                                        jwtAuthenticationFilter,
                                        UsernamePasswordAuthenticationFilter.class)
                                .build();
        }
}
// curl -v \ -H "Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwidXNlcm5hbWUiOiJzdHJpbmciLCJpYXQiOjE3ODMzNzczODUsImV4cCI6MTc4MzM4MDk4NX0.j2zx1odTpaF2PqzyaovpYiCXQDnWDOL-VaOvZL2RHEE" \ http://localhost:8080/api/pokemon/storage?page=0&size=1
