package fitness_app_be.fitness_app.config;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.SecurityFilterChain;

import java.util.Collections;

@Configuration
@Profile("test") // Ensure this configuration is only active in the test profile
public class TestSecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Disable CSRF for testing
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/meals/**").hasRole("ADMIN") // Require ROLE_ADMIN for /api/meals/**
                        .anyRequest().permitAll() // Allow all other requests
                )
                .httpBasic(httpBasic -> httpBasic.realmName("Test Security")) // Use basic authentication
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint((request, response, authException) ->
                                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized"))
                );

        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return email -> {
            if ("admin@example.com".equals(email)) { // Use email instead of username
                return new org.springframework.security.core.userdetails.User(
                        "admin@example.com", // Email as the principal
                        "password", // Mocked password
                        Collections.singletonList(new SimpleGrantedAuthority("ROLE_ADMIN"))
                );
            } else if ("user@example.com".equals(email)) {
                return new org.springframework.security.core.userdetails.User(
                        "user@example.com", // Email as the principal
                        "password", // Mocked password
                        Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))
                );
            } else {
                throw new UsernameNotFoundException("User not found with email: " + email);
            }
        };
    }
}
