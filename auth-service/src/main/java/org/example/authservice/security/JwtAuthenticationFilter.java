package org.example.authservice.security;

import java.io.IOException;
import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  private final JwtTokenProvider jwtTokenProvider;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {
    String token = extractTokenFromHeader(request);

    if (token != null && jwtTokenProvider.validateToken(token)) {
      String userId = jwtTokenProvider.getUserIdFromToken(token).toString();
      String role = jwtTokenProvider.getRoleFromToken(token);

      UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
          userId, null,
          List.of(new SimpleGrantedAuthority("ROLE_" + role)));

      SecurityContextHolder.getContext().setAuthentication(authentication);
      log.debug("Authenticated user: {} with role: {}", userId, role);
    }

    filterChain.doFilter(request, response);
  }

  private String extractTokenFromHeader(HttpServletRequest request) {
    String bearerToken = request.getHeader("Authorization");
    if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
      return bearerToken.substring(7);
    }
    return null;
  }
}
