package com.tdt.shop.controllers;

import com.tdt.shop.dtos.UserDTO;
import com.tdt.shop.dtos.UserLoginDTO;
import com.tdt.shop.exceptions.DataNotFoundException;
import com.tdt.shop.models.User;
import com.tdt.shop.responses.LoginResponse;
import com.tdt.shop.responses.MessageResponse;
import com.tdt.shop.services.IUserService;
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
  private final IUserService userService;
  @PostMapping("/register")
  public ResponseEntity<?> createUser (
    @Valid @RequestBody UserDTO userDTO,
    BindingResult result
  ) {
    try {
      if (result.hasErrors()) {
        List<String> errorMessages = result.getFieldErrors()   // lấy danh sách lỗi
          .stream()              // .stream() của java 8. Ở đây thì lấy 1 trường nào đó trong danh sách và ánh xạ sang mảng khác
          .map(FieldError::getDefaultMessage)        // ánh xạ
          .toList();
        return ResponseEntity.badRequest().body(new MessageResponse(errorMessages.toString()));
      }
      if (!userDTO.getPassword().equals(userDTO.getRetypePassword())) {
        return ResponseEntity.badRequest().body(new MessageResponse("Mật khẩu chưa khớp"));
      }
      User user = userService.createUser(userDTO);
      return ResponseEntity.ok(user);
    }
    catch (Exception e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageResponse(e.getMessage()));
    }
  }

  @PostMapping("/login")
  public ResponseEntity<?> login (
    @Valid @RequestBody UserLoginDTO userLoginDTO,
    BindingResult result
  ) {
    if (result.hasErrors()) {
      List<String> errorMessages = result.getFieldErrors()   // lấy danh sách lỗi
        .stream()              // .stream() của java 8. Ở đây thì lấy 1 trường nào đó trong danh sách và ánh xạ sang mảng khác
        .map(FieldError::getDefaultMessage)        // ánh xạ
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
}
