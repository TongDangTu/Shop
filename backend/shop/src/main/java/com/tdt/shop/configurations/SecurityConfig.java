package com.tdt.shop.configurations;

import com.tdt.shop.models.User;
import com.tdt.shop.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
  // user's detail object
  private final UserRepository userRepository;
  @Bean
  public UserDetailsService userDetailsService () {
    // Trả về trường dữ liệu duy nhất của user.
    return phoneNumber -> {
      User existingUser = userRepository.findByPhoneNumber(phoneNumber)
        .orElseThrow(() -> new UsernameNotFoundException("Cannot find user with phone number: "+ phoneNumber));
      return existingUser;
    };
  }

  @Bean
  public PasswordEncoder passwordEncoder () {
//    return new PasswordEncoder() {
//      // Phương thức này dùng để mã hóa mật khẩu
//      @Override
//      public String encode(CharSequence rawPassword) {
//        // Tự mã hóa theo cách riêng
//        return new StringBuilder(rawPassword).reverse().toString() + rawPassword;
//      }
//
//      // Phương thức này dùng để kiểm tra rawPassword có tương ứng với encodedPassword hay không
//      @Override
//      public boolean matches(CharSequence rawPassword, String encodedPassword) {
//        return new StringBuilder(rawPassword).reverse().toString().equals(encodedPassword);
//      }
//    };

    // Để đơn giản hơn, ta sử dụng ngay một thuật toán mã hóa có sẵn
    return new BCryptPasswordEncoder();
  }

  // Xác thực người dùng
  @Bean
  public AuthenticationProvider authenticationProvider () {
    DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();

    // Sử dụng userDetailsService() và passwordEncoder() ở trên lấy thông tin người dùng và kiểm tra mật khẩu
    authenticationProvider.setUserDetailsService(userDetailsService());
    authenticationProvider.setPasswordEncoder(passwordEncoder());
    return authenticationProvider;
  }

  @Bean
  public AuthenticationManager authenticationManager (AuthenticationConfiguration config) throws Exception {
    return config.getAuthenticationManager();
  }
}
