package com.foodApp.repository;

import com.foodApp.model.IngredientCategory;
import com.foodApp.model.IngredientsItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IngredientCategoryRepository extends JpaRepository<IngredientCategory, Long> {
    List<IngredientCategory> findByRestaurantId(Long id);
    ;
}
