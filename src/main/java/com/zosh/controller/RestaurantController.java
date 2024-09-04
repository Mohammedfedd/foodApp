package com.zosh.controller;

import java.util.List;
import java.util.stream.Collectors;

import com.zosh.domain.RestaurantStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.zosh.Exception.RestaurantException;
import com.zosh.Exception.UserException;
import com.zosh.dto.RestaurantDto;
import com.zosh.model.Restaurant;
import com.zosh.model.User;
import com.zosh.service.RestaurantService;
import com.zosh.service.UserService;

@RestController
@RequestMapping("/api/restaurants")
public class RestaurantController {
	
	@Autowired
	private RestaurantService restaurantService;
	
	@Autowired
	private UserService userService;


	@GetMapping("/search")
	public ResponseEntity<List<Restaurant>> findRestaurantByName(
			@RequestParam String keyword) {
		List<Restaurant> restaurant = restaurantService.searchRestaurant(keyword);

		return ResponseEntity.ok(restaurant);
	}

	@GetMapping()
	public ResponseEntity<List<Restaurant>> getAllRestaurants(
			@RequestHeader("Authorization") String jwt
	) throws Exception {
		User user = userService.findUserProfileByJwt(jwt);
		List<Restaurant> restaurants = restaurantService.getAllRestaurant();

		// Filter out archived restaurants
		List<Restaurant> filteredRestaurants = restaurants.stream()
				.filter(restaurant -> !RestaurantStatus.ARCHIVED.equals(restaurant.getStatus()))
				.collect(Collectors.toList());

		return new ResponseEntity<>(filteredRestaurants, HttpStatus.OK);
	}


	@GetMapping("/{id}")
	public ResponseEntity<Restaurant> findRestaurantById(
			@PathVariable Long id) throws RestaurantException {

			Restaurant restaurant = restaurantService.findRestaurantById(id);
			return ResponseEntity.ok(restaurant);

	}
	
	@PutMapping("/{id}/add-favorites")
	public ResponseEntity<RestaurantDto> addToFavorite(
			@RequestHeader("Authorization") String jwt,
			@PathVariable Long id) throws RestaurantException, UserException {
		
			User user = userService.findUserProfileByJwt(jwt);
			RestaurantDto restaurant = restaurantService.addToFavorites(id, user);
			return ResponseEntity.ok(restaurant);

	}
	
	


}
