package com.example.demo.recipe.service;

import com.example.demo.recipe.DTO.LoginRequestDTO;
import com.example.demo.recipe.DTO.RegisterRequestDTO;
import com.example.demo.recipe.model.User;
import com.example.demo.recipe.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

// gets register request
@Service
@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  public User register(RegisterRequestDTO request) {
// checks username
    if (userRepository.existsByUsername(request.getUsername())) {
      throw new IllegalArgumentException("Username is already taken");
    }

    if (userRepository.existsByEmail(request.getEmail())) {
      throw new IllegalArgumentException("Email is already registered");
    }

    //if everything ok, make user and save to db

    User user = new User();

    user.setUsername(request.getUsername());
    user.setEmail(request.getEmail());

    // then hashes password
    user.setPasswordHash(passwordEncoder.encode(request.getPassword()));


    //then makes entity
    return userRepository.save(user);


  }

  public User login(LoginRequestDTO request) {

    User user = userRepository.findByUsername(request.getLogin())
            .orElseGet(() -> userRepository.findByEmail(request.getLogin())
                    .orElseThrow(() ->
                            new IllegalArgumentException("Invalid username/email or password")
                    ));

    boolean passwordMatches = passwordEncoder.matches(
            request.getPassword(),
            user.getPasswordHash()
    );

    if (!passwordMatches) {
      throw new IllegalArgumentException("Invalid username/email or password");
    }

    return user;
  }


}