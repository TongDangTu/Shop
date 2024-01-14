package com.tdt.shop.services;

import com.tdt.shop.exceptions.DataNotFoundException;
import com.tdt.shop.models.Role;
import com.tdt.shop.repositories.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleService implements IRoleService {
  private final RoleRepository roleRepository;
  @Override
  @Transactional
  public Role createRole() {
    return null;
  }

  @Override
  @Transactional
  public Role updateRole(Long id, Role role) {
    return null;
  }

  @Override
  @Transactional
  public void deleteRole(Long id) {

  }

  @Override
  public Role getRole(Long id) throws DataNotFoundException {
    return roleRepository.findById(id)
      .orElseThrow(() -> new DataNotFoundException("Không tìm thấy Quyền có id: "));
  }

  @Override
  public List<Role> getRoles() {
    return roleRepository.findAll();
  }
}
