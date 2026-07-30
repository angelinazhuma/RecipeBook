package com.example.demo.recipe.model;

// описывает рецепт, соотвествует таблице в базе данных,
// спринг бут и jta используют этот класс для соханения и получения рецептов из базы данных
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity //говорит йаве что класс связан с таблицей в базе
@Table(name = "recipes")
public class Recipe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @NotBlank(message = "Recipe name is required")
    private String name;

    @NotBlank(message = "Author name is required")
    private String author;

    @NotBlank(message = "Recipe Description name is required")
    private String recipeDescription;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL, orphanRemoval = true)
    // один рецепт, может содержать много ингредиентов, связб описнаан в классе ингелридент, операци с рецептом применятеся к из ингрединетам, удаленные з списка ингредиенты удаляеются из бд
    private List<Ingredient> ingredients = new ArrayList<>();

    public Recipe() {
        this.createdAt = LocalDateTime.now();

    }

    public Recipe(String name, String author, String recipeDescription, List<Ingredient> ingredients) {
        this.name = name;
        this.author = author;
        this.recipeDescription = recipeDescription;
        this.ingredients = ingredients;
        this.createdAt = LocalDateTime.now();

    }




}