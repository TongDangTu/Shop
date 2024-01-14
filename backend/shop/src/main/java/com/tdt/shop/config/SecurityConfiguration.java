package com.tdt.shop.config;

import com.tdt.shop.models.Role;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.http.HttpMethod.*;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {
    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;
    private String apiPrefix = "api/v1";
    @Bean
    public SecurityFilterChain securityFilterChain (HttpSecurity http) throws Exception {
        http
            .csrf()
            .disable()
            .authorizeHttpRequests()

            .requestMatchers(apiPrefix+"/users/**")
            .permitAll()
            .requestMatchers(apiPrefix+"/roles/**")
            .permitAll()

            // Categories
            .requestMatchers(GET,apiPrefix+"/categories")
            .permitAll()

            .requestMatchers(POST, apiPrefix+"/categories")
            .hasAnyRole(Role.ADMIN)

            .requestMatchers(PUT, apiPrefix+"/categories/*")
            .hasAnyRole(Role.ADMIN)

            .requestMatchers(DELETE, apiPrefix+"/categories/*")
            .hasAnyRole(Role.ADMIN)

            // Products
            .requestMatchers(GET,apiPrefix+"/products")
            .permitAll()

            .requestMatchers(GET,apiPrefix+"/products/*")
            .permitAll()

            .requestMatchers(POST, apiPrefix+"/products")
            .hasAnyRole(Role.ADMIN)

            .requestMatchers(POST, apiPrefix+"/products/uploads/*")
            .hasAnyRole(Role.ADMIN)

            .requestMatchers(PUT, apiPrefix+"/products/*")
            .hasAnyRole(Role.ADMIN)

            .requestMatchers(DELETE, apiPrefix+"/products/*")
            .hasAnyRole(Role.ADMIN)

            // Orders
            .requestMatchers(GET, apiPrefix+"/orders/user/*")
            .hasAnyRole(Role.USER, Role.ADMIN)

            .requestMatchers(GET, apiPrefix+"/orders/*")
            .hasAnyRole(Role.USER, Role.ADMIN)

            .requestMatchers(POST, apiPrefix+"/orders")
            .hasAnyRole(Role.USER)

            .requestMatchers(PUT, apiPrefix+"/orders/*")
            .hasAnyRole(Role.USER)

            .requestMatchers(DELETE, apiPrefix+"/orders/*")
            .hasAnyRole(Role.USER)

            // OrderDetails
            .requestMatchers(GET, apiPrefix+"/order_details/order/*")
            .hasAnyRole(Role.USER, Role.ADMIN)

            .requestMatchers(GET, apiPrefix+"/order_details/*")
            .hasAnyRole(Role.USER, Role.ADMIN)

            .requestMatchers(POST, apiPrefix+"/order_details")
            .hasAnyRole(Role.USER)

            .requestMatchers(PUT, apiPrefix+"/order_details/*")
            .hasAnyRole(Role.USER)

            .requestMatchers(DELETE, apiPrefix+"/order_details/*")
            .hasAnyRole(Role.USER)


//            .requestMatchers("/api/v1/auth/**", "/api/v1/demobyme/**")
//            .permitAll()
//
//            .requestMatchers(GET,"/api/v1/demoforuser")
//            .hasAnyRole(Role.USER)
//
//            .requestMatchers(GET,"/api/v1/demoforadmin")
//            .hasAnyRole(Role.ADMIN)
//
//            .requestMatchers(GET,"/api/v1/demoforall")
//            .hasAnyRole(Role.USER, Role.ADMIN)

            .anyRequest()
            .authenticated()

            .and()
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .authenticationProvider(authenticationProvider)
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }


}
