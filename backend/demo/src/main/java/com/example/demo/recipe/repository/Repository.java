package com.example.demo.recipe.repository;

import com.example.demo.recipe.model.Recipe; // имопртируем класс рецепты чтобы репозиторий понимал с чем работает
import org.springframework.data.jpa.repository.JpaRepository; //благодаря этому сразу получаем метожы


// repository works with database, spring automatically makes all methods for saving and deleting recipes


@org.springframework.stereotype.Repository // this tells spring that this class works with db
public interface Repository extends JpaRepository<Recipe, Long> {
}

//