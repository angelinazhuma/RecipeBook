package com.example.demo.authorization.service;

import com.example.demo.authorization.DTO.LoginRequestDTO;
import com.example.demo.authorization.DTO.RegisterRequestDTO;
import com.example.demo.authorization.model.User;
import com.example.demo.authorization.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.example.demo.authorization.model.Role;
// gets register request
@Service
@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  public User register(RegisterRequestDTO request) {
// checks username
    if (
        userRepository.existsByUsername(request.getUsername())
            || userRepository.existsByEmail(request.getEmail())
    ) {
      throw new IllegalArgumentException(
          "Username or email is already registered"
      );
    }

    //if everything ok, make user and save to db

    User user = User.builder()
        .username(request.getUsername())
        .email(request.getEmail())
        .passwordHash(
            passwordEncoder.encode(
                request.getPassword()
            )
        )
        .role(Role.USER)
        .build();


    //then makes entity
    return userRepository.save(user);

  }

  public User login(LoginRequestDTO request) {

    User user = userRepository
        .findByUsername(request.getLogin())
        .orElseGet(() ->
            userRepository
                .findByEmail(request.getLogin())
                .orElse(null)
        );

    if (user == null) {
      return null;
    }

    boolean passwordMatches =
        passwordEncoder.matches(
            request.getPassword(),
            user.getPasswordHash()
        );

    if (!passwordMatches) {
      return null;
    }

    return user;
  }

}