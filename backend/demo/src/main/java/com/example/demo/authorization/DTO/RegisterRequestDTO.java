package com.example.demo.authorization.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RegisterRequestDTO {

  @NotBlank(message = "Username is required")
  @Size(min = 3, max = 50)
  private String username;

  @NotBlank(message = "Email is required")
  private String email;

  @NotBlank(message = "Password is required")
  @Size(min = 8, message = "Password must contain at least 8 characters")

  @Pattern(
      regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).*$",
      message = "Password must contain uppercase, lowercase, special symbol and number"
  )
  private String password;
}