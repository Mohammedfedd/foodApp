package com.foodApp.controller;

import com.foodApp.model.Food;
import com.foodApp.model.Restaurant;
import com.foodApp.model.User;
import com.foodApp.request.CreateFoodRequest;
import com.foodApp.response.MessagResponse;
import com.foodApp.service.FoodService;
import com.foodApp.service.FoodServiceImp;
import com.foodApp.service.RestaurantService;
import com.foodApp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/food")
public class AdminFoodController {

    @Autowired
    private FoodService foodService;
    @Autowired
    private UserService userService;
    @Autowired
    private RestaurantService restaurantService;

    @PostMapping
    public ResponseEntity<Food> createFood(@RequestBody CreateFoodRequest req,
                                           @RequestHeader("Authorization") String jwt) throws Exception {

        User user = userService.findUserByJwtToken(jwt);

        Restaurant restaurant = restaurantService.findRestaurantById(req.getRestaurantId());
        Food food = foodService.createFood(req, req.getCategory().restaurant);
        return new ResponseEntity<>(food, HttpStatus.CREATED);


    }

    @DeleteMapping("/{id}")
    public ResponseEntity<MessagResponse> deleteFood(@PathVariable Long id,
                                                     @RequestHeader("Authorization") String jwt) throws Exception {
        User user = userService.findUserByJwtToken(jwt);
        foodService.deleteFood(id);
        MessagResponse res = new MessagResponse();
        res.setMessage("food deleted successfully");
        return new ResponseEntity<>(res, HttpStatus.CREATED);

    }

    @PutMapping("/{id}")
    public ResponseEntity<Food> updateFoodAvaibilityStatus(@PathVariable Long id,
                                                     @RequestHeader("Authorization") String jwt) throws Exception {
        User user = userService.findUserByJwtToken(jwt);
        Food food=foodService.updateAvailiblityStatus(id);

        return new ResponseEntity<>(food, HttpStatus.CREATED);

    }

}