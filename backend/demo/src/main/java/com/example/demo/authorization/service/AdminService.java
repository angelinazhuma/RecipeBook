package com.example.demo.authorization.service;

import com.example.demo.authorization.model.User;
import com.example.demo.authorization.repository.UserRepository;
import com.example.demo.recipe.model.Recipe;
import com.example.demo.recipe.repository.RecipeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminService {

  private final UserRepository userRepository;
  private final RecipeRepository recipeRepository;

  public Page<User> getUsers(
      String search,
      Pageable pageable
  ) {

    if (search == null || search.isBlank()) {
      return userRepository.findAll(pageable);
    }

    return userRepository
        .findByUsernameContainingIgnoreCaseOrEmailContainingIgnoreCase(
            search,
            search,
            pageable
        );
  }

  public List<Recipe> getUserRecipes(Long userId) {
    return recipeRepository.findAllByUserId(userId);
  }
}