package com.tdt.shop.services;

import com.tdt.shop.components.JwtTokenUtil;
import com.tdt.shop.dtos.UserDTO;
import com.tdt.shop.exceptions.DataNotFoundException;
import com.tdt.shop.exceptions.PermissionDenyException;
import com.tdt.shop.models.Role;
import com.tdt.shop.models.User;
import com.tdt.shop.repositories.RoleRepository;
import com.tdt.shop.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserSerVice implements IUserService {
  private final UserRepository userRepository;
  private final RoleRepository roleRepository;
  private final PasswordEncoder passwordEncoder;
  private final AuthenticationManager authenticationManager;
  private final JwtTokenUtil jwtTokenUtil;
  @Override
  public User createUser(UserDTO userDTO) throws Exception {
    // Kiểm tra xem số điện thoại đã tồn tại hay chưa
    String phoneNumber = userDTO.getPhoneNumber();
    if (userRepository.existsByPhoneNumber(phoneNumber)) {
      throw new DataIntegrityViolationException("Số điện thoại hoặc mật khẩu chưa chính xác");
    }
    Role role = roleRepository.findById(userDTO.getRoleId())
      .orElseThrow(() -> new DataNotFoundException("Không tìm thấy quyền"));
    if (role.getName().toUpperCase().equals(Role.ADMIN)) {
      throw new PermissionDenyException("Không thể đăng ký tài khoản với quyền Admin");
    }

    // Convert UserDTO sang User
    User newUser = User.builder()
      .fullName(userDTO.getFullName())
      .phoneNumber(userDTO.getPhoneNumber())
      .address(userDTO.getAddress())
      .password(userDTO.getPassword())
      .dateOfBirth(userDTO.getDateOfBirth())
      .facebookAccountId(userDTO.getFacebookAccountId())
      .googleAccountId(userDTO.getGoogleAccountId())
      .build();
    newUser.setRole(role);
    // Kiểm tra nếu không có accountId thì yêu cầu password. Mã hóa mật khẩu
    if (userDTO.getFacebookAccountId() == 0 && userDTO.getGoogleAccountId() == 0) {
      String password = userDTO.getPassword();
      //
      String encodePassword = passwordEncoder.encode(password);
      newUser.setPassword(encodePassword);
    }
    return userRepository.save(newUser);
  }

  @Override
  public String login(String phoneNumber, String password) throws Exception {
    User existingUser = userRepository.findByPhoneNumber(phoneNumber)
      .orElseThrow(() -> new DataNotFoundException("Số điện thoại hoặc mật khẩu chưa chính xác"));
    // Check password
    if (existingUser.getFacebookAccountId() == 0 && existingUser.getGoogleAccountId() == 0) {
      if (!passwordEncoder.matches(password, existingUser.getPassword())) {
        throw new BadCredentialsException("Số điện thoại hoặc mật khẩu chưa chính xác");
      }
    }

    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
      phoneNumber, password, existingUser.getAuthorities()
    );
    // authenticate with JavaSpring security
    authenticationManager.authenticate(authenticationToken);

    return jwtTokenUtil.generateToken(existingUser);
  }
}
