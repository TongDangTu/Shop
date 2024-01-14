package com.tdt.shop.services;

import com.tdt.shop.dtos.UserDTO;

public interface IUserService {
  String createUser (UserDTO userDTO) throws Exception;
  String login (String phoneNumber, String password) throws Exception;

}
