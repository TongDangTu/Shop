package com.tdt.shop.configurations;

import com.tdt.shop.filters.JwtTokenFilter;
import com.tdt.shop.models.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.CorsConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.Arrays;
import java.util.List;

import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.DELETE;
import static org.springframework.http.HttpMethod.PUT;
import static org.springframework.http.HttpMethod.POST;

@Configuration
@EnableMethodSecurity
@EnableWebMvc
@RequiredArgsConstructor
public class WebSecurityConfig {
  private final JwtTokenFilter jwtTokenFilter;
  @Value("${api.prefix}")
  private String apiPrefix;
  // Bộ lọc bảo mật
  @Bean
  public SecurityFilterChain securityFilterChain (HttpSecurity http) throws Exception {
    http
      .csrf(AbstractHttpConfigurer::disable)
      .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class)
      .authorizeHttpRequests(requests -> {
        requests
          .requestMatchers(
            String.format(apiPrefix + "/users/register"),
            String.format(apiPrefix + "/users/login"),
            String.format(apiPrefix + "/roles"),
            String.format(apiPrefix + "/roles/**")
          )
          .permitAll()

          .requestMatchers(
            GET,
            String.format(apiPrefix + "/categories/**"))
          .hasAnyRole(Role.USER, Role.ADMIN)

          .requestMatchers(
            POST,
            String.format(apiPrefix + "/categories/**"))
          .hasAnyRole(Role.ADMIN)

          .requestMatchers(
            PUT,
            String.format(apiPrefix + "/categories/**"))
          .hasAnyRole(Role.USER, Role.ADMIN)

          .requestMatchers(
            DELETE,
            String.format(apiPrefix + "/categories/**"))
          .hasAnyRole(Role.USER, Role.ADMIN)

          .requestMatchers(
            GET,
            String.format(apiPrefix + "/products/**"))
          .hasAnyRole(Role.USER, Role.ADMIN)

          .requestMatchers(
            POST,
            String.format(apiPrefix + "/products/**"))
          .hasAnyRole(Role.ADMIN)

          .requestMatchers(
            PUT,
            String.format(apiPrefix + "/products/**"))
          .hasAnyRole(Role.USER, Role.ADMIN)

          .requestMatchers(
            DELETE,
            String.format(apiPrefix + "/products/**"))
          .hasAnyRole(Role.USER, Role.ADMIN)

          .requestMatchers(
            GET,
            String.format(apiPrefix + "/orders/**"))
          .hasAnyRole(Role.USER, Role.ADMIN)

          .requestMatchers(
            POST,
            String.format(apiPrefix + "/orders/**"))
          .hasAnyRole(Role.USER)

          .requestMatchers(
            PUT,
            String.format(apiPrefix + "/orders/**"))
          .hasRole(Role.ADMIN)

          .requestMatchers(
            DELETE,
            String.format(apiPrefix + "/orders/**"))
          .hasRole(Role.ADMIN)

          .requestMatchers(
            GET,
            String.format(apiPrefix + "/order_details/**"))
          .hasAnyRole(Role.USER, Role.ADMIN)

          .requestMatchers(
            POST,
            String.format(apiPrefix + "/order_details/**"))
          .hasAnyRole(Role.USER)

          .requestMatchers(
            PUT,
            String.format(apiPrefix + "/order_details/**"))
          .hasRole(Role.ADMIN)

          .requestMatchers(
            DELETE,
            String.format(apiPrefix + "/order_details/**"))
          .hasRole(Role.ADMIN)

          .anyRequest().authenticated();
      })
      .csrf(AbstractHttpConfigurer::disable);
    http.cors(new Customizer<CorsConfigurer<HttpSecurity>>() {
      @Override
      public void customize(CorsConfigurer<HttpSecurity> httpSecurityCorsConfigurer) {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("*"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("authorization", "content-type", "x-auth-token"));
        configuration.setExposedHeaders(List.of("x-auth-token"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        httpSecurityCorsConfigurer.configurationSource(source);
      }
    });
    return http.build();
  }
}
