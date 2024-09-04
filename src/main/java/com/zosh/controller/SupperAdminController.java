package com.zosh.controller;

import java.util.List;
import java.util.stream.Collectors;

import com.zosh.domain.RestaurantStatus;
import com.zosh.model.ArchivedRestaurant;
import com.zosh.model.Restaurant;
import com.zosh.response.ApiResponse;
import com.zosh.service.ArchivedRestaurantService;
import com.zosh.service.CartSerive;
import com.zosh.service.RestaurantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.zosh.model.User;
import com.zosh.repository.UserRepository;
import com.zosh.service.UserService;

@RestController
public class SupperAdminController {

	@Autowired
	private UserService userService;

	@Autowired
	private RestaurantService restaurantService;
	@Autowired
	private CartSerive cartService;
	@Autowired
	private ArchivedRestaurantService archivedRestaurantService;

	
	@GetMapping("/api/customers")
	public ResponseEntity<List<User>> getAllCustomers() {
		
		List<User> users =userService.findAllUsers();
		
		return new ResponseEntity<>(users,HttpStatus.ACCEPTED);

	}

	@DeleteMapping("/api/customers/{id}")
	public ResponseEntity<ApiResponse> deleteCustomer(
			@RequestHeader("Authorization") String jwt,
			@PathVariable Long id
	) throws Exception {
		User user = userService.findUserProfileByJwt(jwt);
		userService.deleteUserById(id);

		ApiResponse res = new ApiResponse();
		res.setMessage("Customer deleted successfully");
		return new ResponseEntity<>(res, HttpStatus.OK);
	}
	@GetMapping("/api/restaurants/restaurantList")
	public ResponseEntity<List<Restaurant>> getAllRestaurantList(
			@RequestHeader("Authorization") String jwt
	) throws Exception {
		List<Restaurant> restaurants = restaurantService.getAllRestaurant();
		// Filter out archived restaurants
		List<Restaurant> filteredRestaurants = restaurants.stream()
				.filter(restaurant -> !RestaurantStatus.ACTIVE.equals(restaurant.getStatus()))
				.collect(Collectors.toList());

		return new ResponseEntity<>(restaurants, HttpStatus.OK);
	}


	@PostMapping("/api/restaurants/{id}/archive")
	public ResponseEntity<Void> archiveRestaurant(@PathVariable Long id) throws Exception {
		restaurantService.archiveRestaurant(id);
		return ResponseEntity.noContent().build();
	}
	@PostMapping("/api/restaurants/{id}/unarchive")
	public ResponseEntity<Void> unarchiveRestaurant(@PathVariable Long id) throws Exception {
		restaurantService.unarchiveRestaurant(id);
		return ResponseEntity.noContent().build();
	}
	@GetMapping("/api/restaurants/archived")
	public ResponseEntity<List<ArchivedRestaurant>> getAllArchivedRestaurants() {
		List<ArchivedRestaurant> archivedRestaurants = archivedRestaurantService.getAllArchivedRestaurants();
		return ResponseEntity.ok(archivedRestaurants);
	}

}
