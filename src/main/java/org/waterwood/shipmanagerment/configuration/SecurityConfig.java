package org.waterwood.shipmanagerment.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.waterwood.shipmanagerment.infrastructure.authentication.JwtAccessDeniedHandler;
import org.waterwood.shipmanagerment.infrastructure.authentication.JwtAuthenticationEntryPoint;
import org.waterwood.shipmanagerment.infrastructure.component.OAuth2JwtDecoder;
import org.waterwood.shipmanagerment.infrastructure.component.UserJwtAuthConverter;

import java.util.List;

@Configuration
@EnableWebSecurity(debug = false)
@RequiredArgsConstructor
public class SecurityConfig {
    private final OAuth2JwtDecoder oauth2JwtDecoder;
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, UserJwtAuthConverter jwtConverter, JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint, JwtAccessDeniedHandler jwtAccessDeniedHandler) throws Exception {
        return http
                .cors(c-> c.configurationSource(corsConfigurationSource()))
                .csrf(c-> c.disable())
                .authorizeHttpRequests(
                        auth ->
                                auth.requestMatchers("/api/auth/**").permitAll()
                                .anyRequest().authenticated()
                )
                .oauth2ResourceServer(
                    oauth2 ->
                            oauth2.jwt(jwt -> jwt
                                    .decoder(oauth2JwtDecoder)
                                    .jwtAuthenticationConverter(jwtConverter))
                )
                .exceptionHandling(
                        e
                                -> e.accessDeniedHandler(jwtAccessDeniedHandler)
                                .authenticationEntryPoint(jwtAuthenticationEntryPoint))
                .formLogin(form -> form.disable())
                .build();

    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedHeaders(List.of("*"));
        config.setAllowedOrigins(List.of(
                "http://localhost:5180"
        ));
        config.setAllowedMethods(List.of(
                "GET", "POST", "PUT", "DELETE", "OPTIONS"
        ));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
     }
}
