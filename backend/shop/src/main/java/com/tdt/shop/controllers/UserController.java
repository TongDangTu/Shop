package com.tdt.shop.controllers;

import com.tdt.shop.dtos.UserChangePasswordDTO;
import com.tdt.shop.dtos.UserDTO;
import com.tdt.shop.dtos.UserLoginDTO;
import com.tdt.shop.exceptions.InvalidParamException;
import com.tdt.shop.models.User;
import com.tdt.shop.responses.LoginResponse;
import com.tdt.shop.responses.MessageResponse;
import com.tdt.shop.responses.UserResponse;
import com.tdt.shop.services.IUserService;
import com.tdt.shop.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/users")
public class UserController {
  private final UserService userService;
  @PostMapping("/register")
  public ResponseEntity<?> createUser (
    @Valid @RequestBody UserDTO userDTO,
    BindingResult result
  ) {
    try {
      if (result.hasErrors()) {
        List<String> errorMessages = result.getFieldErrors()
          .stream()
          .map(FieldError::getDefaultMessage)
          .toList();
        return ResponseEntity.badRequest().body(new MessageResponse(errorMessages.toString()));
      }
      if (!userDTO.getPassword().equals(userDTO.getRetypePassword())) {
        return ResponseEntity.badRequest().body(new MessageResponse("Mật khẩu chưa khớp"));
      }
      String token = userService.createUser(userDTO);
      LoginResponse registerResponse = LoginResponse.builder()
        .message("Đăng ký thành công")
        .token(token)
        .build();
      return ResponseEntity.ok(registerResponse);
    }
    catch (Exception e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageResponse(e.getMessage()));
    }
  }

  @PutMapping("/{id}")
  public ResponseEntity<?> updateUser (
    @PathVariable Long id,
    @RequestBody UserDTO userDTO,
    @RequestHeader("Authorization") String authHeader
  ) {
    try {
      String jwt = authHeader.substring(7);
      User user = userService.getUserDetailsByJwt(jwt);
      if (id != user.getId()) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
      }
      user = userService.updateUser(id, userDTO);
      return ResponseEntity.ok(UserResponse.fromUser(user));

    } catch (Exception e) {
      return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
    }
  }

  @PutMapping("/{id}/change_password")
  public ResponseEntity<?> changePassword (
    @PathVariable("id") Long id,
    @RequestBody UserChangePasswordDTO userChangePasswordDTO,
    @RequestHeader("Authorization") String authHeader
  ) {
    try {
      String jwt = authHeader.substring(7);
      User user = userService.getUserDetailsByJwt(jwt);
      if (id != user.getId()) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
      }
      String password = userChangePasswordDTO.getPassword();
      String newPassword = userChangePasswordDTO.getNewPassword();
      String retypePassword = userChangePasswordDTO.getRetypePassword();
      if(!newPassword.equals(retypePassword)) {
        return ResponseEntity.badRequest().body(new MessageResponse("Mật khẩu chưa khớp"));
      }
      user = userService.changePassword(id, password, newPassword);
      return ResponseEntity.ok(UserResponse.fromUser(user));

    } catch (Exception e) {
      return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
    }
  }

  @PostMapping("/login")
  public ResponseEntity<?> login (
    @Valid @RequestBody UserLoginDTO userLoginDTO,
    BindingResult result
  ) {
    if (result.hasErrors()) {
      List<String> errorMessages = result.getFieldErrors()
        .stream()
        .map(FieldError::getDefaultMessage)
        .toList();
      return ResponseEntity.badRequest().body(new MessageResponse(errorMessages.toString()));
    }
    // Kiểm tra thông tin đăng nhập và sinh Token
    try {
      String token = userService.login(userLoginDTO.getPhoneNumber(), userLoginDTO.getPassword());
      LoginResponse loginResponse = LoginResponse.builder()
        .message("Đăng nhập thành công")
        .token(token)
        .build();
    // Trả về token trong response
      return ResponseEntity.ok(loginResponse);
    } catch (Exception e) {
      return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
    }
  }

  @PostMapping("/details")
  public ResponseEntity<?> getUserDetails (
    @RequestHeader("Authorization") String authHeader
  ) {
    try {
      String jwt = authHeader.substring(7);
      User user = userService.getUserDetailsByJwt(jwt);
      return ResponseEntity.ok(UserResponse.fromUser(user));
    }
    catch (Exception e) {
      return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
    }
  }
}
