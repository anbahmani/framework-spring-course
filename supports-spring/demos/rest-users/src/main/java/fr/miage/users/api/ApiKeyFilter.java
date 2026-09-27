package fr.miage.users.api;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

/** Filtre pédagogique, pas une gestion complète de comptes et de droits. */
@Component
public class ApiKeyFilter extends OncePerRequestFilter {
    private final String key;
    public ApiKeyFilter(@Value("${demo.api-key}") String key) { this.key = key; }
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI().substring(request.getContextPath().length());
        return !path.startsWith("/api/");
    }
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain chain) throws ServletException, IOException {
        if (!key.equals(request.getHeader("X-API-KEY"))) {
            response.setStatus(401);
            response.setContentType("application/problem+json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write("""
                {"type":"about:blank","title":"Unauthorized","status":401,
                 "detail":"Clé API manquante ou invalide"}
                """);
            return;
        }
        chain.doFilter(request, response);
    }
}
