package com.app.oudiac.filters;

import com.app.oudiac.services.JWTService.JwtService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.security.sasl.AuthenticationException;
import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtValidationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    // 1. Define the endpoints you want to skip
    private static final List<String> EXCLUDED_URLS = List.of(
            "/api/auth",
            "/api/users/login"
    );

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getRequestURI();

        // 2. If the current request path starts with any of the excluded URLs, skip the filter
        return EXCLUDED_URLS.stream().anyMatch(path::startsWith);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        // 3. Extract the Authorization header
        String authHeader = request.getHeader("Authorization");
        System.out.println("Auth Header : "+authHeader);
        // 4. Check if the header is missing or doesn't start with "Bearer "
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            rejectRequest(response, "Missing or poorly formatted Authorization header");
            return;
        }

        // 5. Extract the actual token (remove "Bearer " prefix)
        String token = authHeader.substring(7);

        try {
            System.out.println(token);
            // 6. VALIDATE YOUR TOKEN HERE
            // Example: jwtService.validateToken(token);
            // If the token is expired or altered, your JWT library should throw an exception.
            jwtService.validateToken(token);
            // If validation is successful, continue the filter chain

            // 2. ONLY if the SecurityContext is empty, we populate it
            if (SecurityContextHolder.getContext().getAuthentication() == null) {

                // Extract username and roles from your token
                Claims claims=jwtService.getClaims(token);
                String username = String.valueOf(claims.get("userName"));
                String role = String.valueOf(claims.get("role")); // e.g., "ADMIN" or "MANAGER"
                System.out.println(username+" - "+role);
                // Convert the role string into a GrantedAuthority
                List<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(role));

                // Create the Authentication object
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        username,
                        null, // Credentials (usually null for JWT)
                        authorities
                );

                // Optional: Attach request details (like IP address) to the token
                 authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // 3. Hand the authentication over to Spring Security!
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }

            filterChain.doFilter(request, response);

        } catch (Exception e) {
            // 7. Catch JWT exceptions (like ExpiredJwtException) and reject
            rejectRequest(response, "Invalid or expired JWT token");
        }
    }

    // Helper method to keep the code clean
    private void rejectRequest(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401
        response.setContentType("application/json");
        response.getWriter().write("{\"error\": \"Unauthorized\", \"message\": \"" + message + "\"}");
    }
}
