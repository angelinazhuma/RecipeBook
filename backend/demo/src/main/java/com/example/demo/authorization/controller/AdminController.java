package com.example.demo.authorization.controller;

import com.example.demo.authorization.model.User;
import com.example.demo.authorization.repository.UserRepository;
import com.example.demo.recipe.model.Recipe;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.demo.recipe.repository.RecipeRepository;

import java.util.List;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

  private final UserRepository userRepository;
  private final RecipeRepository recipeRepository;

  @GetMapping("/users")
  public List<User> getAllUsers() {
    return userRepository.findAll();
  }

  @GetMapping("/users/{userId}/recipes")
  public List<Recipe> getUserRecipes(
      @PathVariable Long userId
  ) {
    return recipeRepository.findAllByUserId(userId);
  }
}