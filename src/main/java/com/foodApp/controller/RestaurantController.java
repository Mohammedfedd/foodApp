package com.foodApp.controller;

import com.foodApp.dto.RestaurantDto;
import com.foodApp.model.Restaurant;
import com.foodApp.model.RestaurantStatus;
import com.foodApp.model.User;
import com.foodApp.request.CreateRestaurantRequest;
import com.foodApp.service.RestaurantService;
import com.foodApp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/restaurants")
public class RestaurantController {
    @Autowired
    private RestaurantService restaurantService;

    @Autowired
    private UserService userService;

    @GetMapping("/search")
    public ResponseEntity<List<Restaurant>> searchRestaurant(
            @RequestHeader("Authorization") String jwt,
            @RequestParam String keyword
    ) throws Exception {
        User user = userService.findUserByJwtToken(jwt);
        List<Restaurant> restaurant = restaurantService.searchRestaurant(keyword);
        return new ResponseEntity<>(restaurant, HttpStatus.OK);
    }

    @GetMapping()
    public ResponseEntity<List<Restaurant>> getAllRestaurants(
            @RequestHeader("Authorization") String jwt
    ) throws Exception {
        User user = userService.findUserByJwtToken(jwt);
        List<Restaurant> restaurants = restaurantService.getAllRestaurant();

        // Filter out archived restaurants
        List<Restaurant> filteredRestaurants = restaurants.stream()
                .filter(restaurant -> !RestaurantStatus.ARCHIVED.equals(restaurant.getStatus()))
                .collect(Collectors.toList());

        return new ResponseEntity<>(filteredRestaurants, HttpStatus.OK);
    }
    @GetMapping("/archived")
    public ResponseEntity<List<Restaurant>> getAllArchivedRestaurants(
            @RequestHeader("Authorization") String jwt
    ) throws Exception {
        User user = userService.findUserByJwtToken(jwt);
        List<Restaurant> restaurants = restaurantService.getAllRestaurant();

        // Filter out archived restaurants
        List<Restaurant> filteredRestaurants = restaurants.stream()
                .filter(restaurant -> !RestaurantStatus.ACTIVE.equals(restaurant.getStatus()))
                .collect(Collectors.toList());

        return new ResponseEntity<>(filteredRestaurants, HttpStatus.OK);
    }


    @GetMapping("/{id}")
    public ResponseEntity<Restaurant> findRestaurantById(
            @RequestHeader("Authorization") String jwt,
            @PathVariable Long id
    ) throws Exception {
        User user = userService.findUserByJwtToken(jwt);
        Restaurant restaurant = restaurantService.findRestaurantById(id);
        return new ResponseEntity<>(restaurant, HttpStatus.OK);
    }
    @PutMapping("/{id}/add-favourites")
    public ResponseEntity<RestaurantDto> addToFavourites(
            @RequestHeader("Authorization") String jwt,
            @PathVariable Long id
    ) throws Exception {
        User user = userService.findUserByJwtToken(jwt);
        RestaurantDto restaurant = restaurantService.addToFavorite(id,user);
        return new ResponseEntity<>(restaurant, HttpStatus.OK);
    }
}