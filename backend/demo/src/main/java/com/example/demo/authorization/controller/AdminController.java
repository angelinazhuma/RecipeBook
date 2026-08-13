package com.example.demo.authorization.controller;

import com.example.demo.authorization.model.User;
import com.example.demo.authorization.service.AdminService;
import com.example.demo.recipe.model.Recipe;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

  private final AdminService adminService;

  @GetMapping("/users")
  public Page<User> getAllUsers(
      @RequestParam(defaultValue = "") String search,
      @PageableDefault(size = 10) Pageable pageable
  ) {
    return adminService.getUsers(
        search,
        pageable
    );
  }

  @GetMapping("/users/{userId}/recipes")
  public List<Recipe> getUserRecipes(
      @PathVariable Long userId
  ) {
    return adminService.getUserRecipes(userId);
  }
}