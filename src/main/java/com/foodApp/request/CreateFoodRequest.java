package com.foodApp.request;

import com.foodApp.model.Category;
import com.foodApp.model.IngredientsItem;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateFoodRequest {

    private String name;
    private String description;
    private Long price;
    private Category category;
    private List<String> images;

    private Long restaurantId;
    private boolean vegetarian;
    private boolean seasional;
    private List<IngredientsItem> ingredients;
}
