package blps.lab2.security;

import blps.lab2.controller.exceptions.AuthenticationFailException;
import blps.lab2.model.domain.user.User;
import blps.lab2.model.responses.ErrorResponse;
import blps.lab2.service.user.JwtService;
import blps.lab2.service.user.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jose.JOSEException;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final UserService userService;
    private final ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        try {
            if (request.getServletPath().contains("/api/v1/auth")) {
                filterChain.doFilter(request, response);
                return;
            }
            String authHeader = request.getHeader("Authorization");
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                filterChain.doFilter(request, response);
                return;
            }
            String token = authHeader.substring(7);
            Map<String, Object> claims;
            claims = jwtService.verifyAndParseClaims(token);


            long userId = Long.parseLong(claims.get("userId").toString());
            Authentication authContext = SecurityContextHolder.getContext().getAuthentication();
            if (authContext == null || ((User) authContext.getPrincipal()).getId() != userId) {
                Optional<User> maybeUser = this.userService.getUserById(userId);
                if (maybeUser.isEmpty()) {
                    filterChain.doFilter(request, response);
                    return;
                }
                User user = maybeUser.get();

                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        user,
                        null,
                        List.of(new SimpleGrantedAuthority(user.getRole().name()))
                );

                authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }

            filterChain.doFilter(request, response);
        } catch (AuthenticationFailException e) {
            handleAuthError(response, e);
        } catch (JOSEException e) {
            throw new RuntimeException(e);
        }
    }

    private void handleAuthError(HttpServletResponse response, Exception e) throws IOException {
        ErrorResponse errorResponse = new ErrorResponse(e.getMessage());

        response.setContentType("application/json");
        response.setStatus(401);
        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
    }
}