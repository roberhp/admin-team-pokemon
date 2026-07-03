package com.pokeadmin.adminteampokemon.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
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
                                .authorizeHttpRequests(auth -> auth
                                        .requestMatchers(
                                                "/api/v1/auth/**",
                                                "/swagger-ui/**",
                                                "/v3/api-docs/**",
                                                "/api/pokedex/**"
                                                )
                                        .permitAll()
                                        .anyRequest()
                                        .authenticated())
                                .addFilterBefore(
                                                jwtAuthenticationFilter,
                                                UsernamePasswordAuthenticationFilter.class)
                                .httpBasic(Customizer.withDefaults())
                                .build();
        }
}