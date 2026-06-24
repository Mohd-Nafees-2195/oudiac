package com.app.oudiac.configs;



import com.app.oudiac.filters.JwtValidationFilter;
import com.app.oudiac.models.enums.Role;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import javax.crypto.SecretKey;
import io.jsonwebtoken.security.MacAlgorithm;
import io.jsonwebtoken.Jwts;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.List;

@Configuration
public class SecurityConfig {

    @Value("${FRONT_END_URL}")
    private String frontEndUrl;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean
    public SecretKey secretKey() {
        MacAlgorithm algorithm = Jwts.SIG.HS256;
        SecretKey secretKey = algorithm.key().build();
        return secretKey;
    }

// ...

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtValidationFilter jwtValidationFilter) throws Exception {

        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(csrf -> csrf.disable())

                // 1. MAKE IT STATELESS (Crucial for JWT)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                .authorizeHttpRequests(auth -> auth
                        // 2. USE hasAuthority() INSTEAD OF hasRole() TO AVOID THE "ROLE_" PREFIX TRAP
                        .requestMatchers("/api/auth/**", "/api/users/login").permitAll()
                        .requestMatchers("/api/admin/oudiac/**","/api/stores/oudiac/**").hasAuthority("ADMIN")
                        .requestMatchers("/api/users/oudiac/**").hasAuthority("USER")
                        .requestMatchers("/api/products/oudiac/**").hasAnyAuthority("ADMIN", "MANAGER")

                        .anyRequest().authenticated()
                )
                .formLogin(form -> form.disable())
                .httpBasic(basic -> basic.disable())

                // 3. ADD YOUR JWT FILTER BEFORE THE STANDARD PASSWORD FILTER
                .addFilterBefore(jwtValidationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOrigins(
                List.of(frontEndUrl)
        );

        configuration.setAllowedMethods(
                List.of("GET", "POST", "PUT", "DELETE", "OPTIONS")
        );

        configuration.setAllowedHeaders(
                List.of("*")
        );

        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source =
                new UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration("/**", configuration);

        return source;
    }
}



//@Bean
//public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//
//    http
//            .csrf(csrf -> csrf.disable())
//            .authorizeHttpRequests(auth -> auth
//
//                    // 🔴 Admin only
//                    .requestMatchers("/api/admin/**").hasRole("ADMIN")
//
//                    // 🟢 User only
//                    .requestMatchers("/api/user/**").hasRole("USER")
//
//                    // 🟡 Both ADMIN & USER
//                    .requestMatchers("/api/common/**").hasAnyRole("ADMIN", "USER")
//
//                    // 🔓 Public APIs (OTP / login)
//                    .requestMatchers("/api/auth/**").permitAll()
//
//                    // 🔒 Everything else needs authentication
//                    .anyRequest().authenticated()
//            )
//            .formLogin(form -> form.disable())
//            .httpBasic(basic -> basic.disable());
//
//    return http.build();
//}
