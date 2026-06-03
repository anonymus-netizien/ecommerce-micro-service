package com.stschool.ecommerce.productservice.config;

import com.stschool.ecommerce.productservice.security.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class AppConfig {

    private final JwtAuthenticationFilter jwtFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
                .sessionManagement(sessionManagement -> sessionManagement.sessionCreationPolicy(
                        SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        // PUBLIC API's
                        .requestMatchers("/api/auth/**").permitAll()
                        // product read
                        .requestMatchers(HttpMethod.GET, "/api/products/**").hasAnyRole("CUSTOMER", "ADMIN")
                        // product save
                        .requestMatchers(HttpMethod.POST, "/api/products/**").hasAnyRole("ADMIN")
                        // product update
                        .requestMatchers(HttpMethod.PUT, "/api/products/**").hasRole("ADMIN")
                        // product delete
                        .requestMatchers(HttpMethod.DELETE, "/api/products/**").hasRole("ADMIN")
                        // remaining API's
                        .anyRequest().authenticated()
                )
                .addFilterBefore(
                        jwtFilter,
                        UsernamePasswordAuthenticationFilter.class
                );
        return http.build();
    }

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
}
