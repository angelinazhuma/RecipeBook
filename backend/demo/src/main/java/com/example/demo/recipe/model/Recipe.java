package com.example.demo.recipe.model;

// описывает рецепт, соотвествует таблице в базе данных,
// спринг бут и jta используют этот класс для соханения и получения рецептов из базы данных
import com.example.demo.authorization.model.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

@Getter
@Setter
@Entity //говорит йаве что класс связан с таблицей в базе
@Table(name = "recipes")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Recipe {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private User user;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Recipe name is required")
    private String name;

    @NotBlank(message = "Author name is required")
    private String author;

    @NotBlank(message = "Recipe Description name is required")
    private String recipeDescription;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL, orphanRemoval = true)
    // один рецепт, может содержать много ингредиентов, связб описнаан в классе ингелридент, операци с рецептом применятеся к из ингрединетам, удаленные з списка ингредиенты удаляеются из бд
    private List<Ingredient> ingredients = new ArrayList<>();
}