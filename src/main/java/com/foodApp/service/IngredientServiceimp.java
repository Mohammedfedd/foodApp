package com.foodApp.service;

import com.foodApp.model.IngredientCategory;
import com.foodApp.model.IngredientsItem;
import com.foodApp.model.Restaurant;
import com.foodApp.repository.IngredientCategoryRepository;
import com.foodApp.repository.IngredientItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class IngredientServiceimp implements IngredientsService {
    @Autowired
    public IngredientItemRepository ingredientItemRepository;
    @Autowired
    private IngredientCategoryRepository ingredientCategoryRepository;
    @Autowired
    private RestaurantService restaurantService;

    @Override
    public IngredientCategory createIngredientCategory(String name, Long restaurantId) throws Exception {
        Restaurant restaurant=restaurantService.findRestaurantById(restaurantId);
        IngredientCategory category=new IngredientCategory();
        category.setRestaurant(restaurant);
        category.setName(name);
        return ingredientCategoryRepository.save(category);
    }

    @Override
    public IngredientCategory findIngredientCategoryById(Long id) throws Exception {
        Optional<IngredientCategory> opt=ingredientCategoryRepository.findById(id);
        if(opt.isEmpty())
        {
            throw new Exception("ingredient category not found");
        }
        return opt.get();
    }

    @Override
    public List<IngredientCategory> findIngredientCategoryByRestaurantId(Long id) throws Exception {
        restaurantService.findRestaurantById(id);
        return ingredientCategoryRepository.findByRestaurantId(id);
    }

    @Override
    public IngredientsItem createIngredientItem(Long restaurantId, String ingredientName, Long categoryId) throws Exception {
        Restaurant restaurant=restaurantService.findRestaurantById(restaurantId);
        IngredientCategory category=findIngredientCategoryById(categoryId);
        IngredientsItem item=new IngredientsItem();
        item.setName(ingredientName);
        item.setRestaurant(restaurant);
        item.setCategory(category);
        IngredientsItem ingredient=ingredientItemRepository.save(item);
        category.getIngredients().add(ingredient);
        return ingredient;
    }

    @Override
    public List<IngredientsItem> findRestaurantIngredients(Long restaurantId) throws Exception {
        return ingredientItemRepository.findByRestaurantId(restaurantId);
    }

    @Override
    public IngredientsItem updateStock(Long id) throws Exception {
        Optional<IngredientsItem> optionalIngredients=ingredientItemRepository.findById(id);
        if(optionalIngredients.isEmpty())
        {
            throw new Exception("ingredient not found");
        }
        IngredientsItem ingredientsItem=optionalIngredients.get();
        ingredientsItem.setInStoke(!ingredientsItem.isInStoke());
        return ingredientItemRepository.save(ingredientsItem);
    }
}
