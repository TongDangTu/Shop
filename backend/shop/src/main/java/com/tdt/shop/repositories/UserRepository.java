package com.tdt.shop.repositories;

import com.tdt.shop.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
  boolean existsByPhoneNumber (String phoneNumber);

//  // SELECT * FROM users WHERE phoneNumber=?
  Optional<User> findByPhoneNumber (String phoneNumber);
}
