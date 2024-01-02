package com.tdt.shop.services;

import com.tdt.shop.exceptions.DataNotFoundException;
import com.tdt.shop.models.Role;

import java.util.List;

public interface IRoleService {
  Role createRole ();

  Role updateRole (Long id, Role role);

  void deleteRole (Long id);

  Role getRole (Long id) throws DataNotFoundException;

  List<Role> getRoles ();
}
