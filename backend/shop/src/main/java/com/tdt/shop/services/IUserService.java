package com.tdt.shop.services;

import com.tdt.shop.dtos.UserDTO;
import com.tdt.shop.exceptions.DataNotFoundException;
import com.tdt.shop.models.User;

public interface IUserService {
  User createUser (UserDTO userDTO) throws Exception;
  String login (String phoneNumber, String password) throws Exception;

}
