package com.tdt.shop.filters;

import com.tdt.shop.components.JwtTokenUtil;
import com.tdt.shop.models.User;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.util.Pair;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.cors.CorsUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

// OncePerRequestFilter: mỗi request đều phải qua ông này để kiểm tra
@Component
@RequiredArgsConstructor
public class JwtTokenFilter extends OncePerRequestFilter {
  @Value("${api.prefix}")
  private String apiPrefix;
  private final UserDetailsService userDetailsService;
  private final JwtTokenUtil jwtTokenUtil;

  @Override
  protected void doFilterInternal(
    @NonNull HttpServletRequest request,
    @NonNull HttpServletResponse response,
    @NonNull FilterChain filterChain
  ) throws ServletException, IOException {
    try {
//      filterChain.doFilter(request, response);    // cho tất cả request đi qua hết
      if (isByPassToken(request)) {     // chỉ cho phép một số các request
        filterChain.doFilter(request, response);
        return;
      }
      final String authHeader = request.getHeader("Authorization");
      if (authHeader == null || !authHeader.startsWith("Bearer")) {
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
        return;
      }
      final String token = authHeader.substring(7);
      final String phoneNumber = jwtTokenUtil.extractPhoneNumber(token);
      if (phoneNumber != null
        && SecurityContextHolder.getContext().getAuthentication() == null) {
        User userDetails = (User) userDetailsService.loadUserByUsername(phoneNumber);
        if (jwtTokenUtil.validateToken(token, userDetails)) {
          UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
            userDetails,
            null,
            userDetails.getAuthorities()
          );
          authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
          SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        }
      }
      filterChain.doFilter(request, response);
    }
    catch (Exception e) {
      response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
    }
  }

  private boolean isByPassToken (
    @NonNull HttpServletRequest request
  ) {
    final List<Pair<String, String>> bypassTokens = Arrays.asList(
      Pair.of(apiPrefix + "/products", "GET"),
      Pair.of(apiPrefix + "/categories", "GET"),
      Pair.of(apiPrefix + "/roles", "GET"),
      Pair.of(apiPrefix + "/users/register", "POST"),
      Pair.of(apiPrefix + "/users/login", "POST")
    );
    for (Pair<String, String> bypassToken : bypassTokens) {
      if (request.getServletPath().contains(bypassToken.getFirst())
            && request.getMethod().equals(bypassToken.getSecond())) {
        return true;
      }
    }
    return false;
  }
}
