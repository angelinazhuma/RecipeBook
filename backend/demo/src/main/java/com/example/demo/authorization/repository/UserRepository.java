package com.example.demo.authorization.repository;

import com.example.demo.authorization.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

  // for user signing in

  Optional<User> findByUsername(String username);

  Optional<User> findByEmail (String email);

  boolean existsByUsername(String username);

  boolean existsByEmail(String email);

  // these methods are for checking if this user already exists

}