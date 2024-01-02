package com.tdt.shop.controllers;

import com.tdt.shop.exceptions.DataNotFoundException;
import com.tdt.shop.models.Role;
import com.tdt.shop.responses.MessageResponse;
import com.tdt.shop.services.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("${api.prefix}/roles")
@RequiredArgsConstructor
public class RoleController {
  private final RoleService roleService;

  @GetMapping("/{id}")
  public ResponseEntity<?> getRole (
    @PathVariable Long id
  ) {
    try {
      return ResponseEntity.ok(roleService.getRole(id));
    } catch (DataNotFoundException e) {
      return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
    }
  }

  @GetMapping("")
  public ResponseEntity<?> getRoles () {
    return ResponseEntity.ok(roleService.getRoles());
  }
}
