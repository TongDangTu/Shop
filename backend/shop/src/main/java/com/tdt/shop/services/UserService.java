package com.tdt.shop.services;

import com.tdt.shop.config.JwtService;
import com.tdt.shop.dtos.UserChangePasswordDTO;
import com.tdt.shop.dtos.UserDTO;
import com.tdt.shop.exceptions.DataNotFoundException;
import com.tdt.shop.exceptions.InvalidParamException;
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
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;

@RequiredArgsConstructor
@Service
public class UserService implements IUserService {
  private final UserRepository userRepository;
  private final RoleRepository roleRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtService jwtService;
  private final AuthenticationManager authenticationManager;

  @Override
  @Transactional
  public String createUser(UserDTO userDTO) throws Exception {
    // Kiểm tra xem số điện thoại đã tồn tại hay chưa
    String phoneNumber = userDTO.getPhoneNumber();
    if (userRepository.existsByPhoneNumber(phoneNumber)) {
      throw new DataIntegrityViolationException("Số điện thoại này đã được sử dụng");
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
    userRepository.save(newUser);
    String jwtToken = jwtService.generateToken(newUser);
    return jwtToken;
  }

  @Override
  public User updateUser (Long id, UserDTO userDTO) throws DataNotFoundException {
    User userExisting = userRepository.findById(id)
      .orElseThrow(() -> new DataNotFoundException("Không tìm thấy user"));
    userExisting.setFullName(userDTO.getFullName());
    userExisting.setAddress(userDTO.getAddress());
    userExisting.setDateOfBirth(userDTO.getDateOfBirth());
    return userRepository.save(userExisting);
  }

  @Override
  public User changePassword (Long id, String password, String newPassword) throws DataNotFoundException, InvalidParamException {
    User userExisting = userRepository.findById(id)
      .orElseThrow(() -> new DataNotFoundException("Không tìm thấy user"));
    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
      userExisting.getPhoneNumber(), password
    );

    // authenticate with JavaSpring security
    authenticationManager.authenticate(authenticationToken);

    String encodePassword = passwordEncoder.encode(newPassword);
    userExisting.setPassword(encodePassword);
    return userRepository.save(userExisting);
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
      phoneNumber, password
    );

    // authenticate with JavaSpring security
    authenticationManager.authenticate(authenticationToken);

    HashMap<String, Object> hashMap = new HashMap<>();
    hashMap.put("user_id", existingUser.getId());

    String jwtToken = jwtService.generateToken(hashMap, existingUser);
    return jwtToken;
  }

  @Override
  public User getUserDetailsByJwt(String jwt) throws Exception {
    if (jwtService.isTokenExpired(jwt)) {
      throw new Exception("Token đã hết hạn");
    }

    String phoneNumber = jwtService.extractUsername(jwt);
    return userRepository.findByPhoneNumber(phoneNumber)
      .orElseThrow(() -> new DataNotFoundException("Không tìm thấy User"));
  }
}
