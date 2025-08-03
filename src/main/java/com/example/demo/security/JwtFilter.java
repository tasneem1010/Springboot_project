package com.example.demo.security;

import java.io.IOException;

import com.example.demo.dto.UserDTO;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;


@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    private final TokenManager tokenManager;
    private final UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            // next filter, if last filter next resource
            filterChain.doFilter(request, response);
            return;
        }

        final String token = authHeader.substring(7);

        if (!tokenManager.validateToken(token)) {
            filterChain.doFilter(request, response);
            return;
        }

        Integer userId = tokenManager.extractUserId(token);
        if (userId == null) {
            filterChain.doFilter(request, response);
            return;
        }
        
        User user = userRepository.findById(userId.intValue());

        if (user != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            //TODO is this right?
            // store all user info in spring context
            UserDTO userDetails = new UserDTO(user);
            // authenticated user
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                    userDetails, null, null);
            // add info to the token ex. session id
            authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            // set the authenticated user to spring context
            // SecurityContextHolder.getContext().getAuthentication() will return this user
            SecurityContextHolder.getContext().setAuthentication(authToken);
        }

        filterChain.doFilter(request, response);
    }
}
