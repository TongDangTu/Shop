package com.tdt.shop.services;

import com.tdt.shop.dtos.UserChangePasswordDTO;
import com.tdt.shop.dtos.UserDTO;
import com.tdt.shop.models.User;

public interface IUserService {
  User getUserDetailsByJwt(String jwt) throws Exception;
  String createUser (UserDTO userDTO) throws Exception;
  User updateUser (Long id, UserDTO userDTO) throws Exception;
  User changePassword (Long id, String password, String newPassword) throws Exception;
  String login (String phoneNumber, String password) throws Exception;
}
