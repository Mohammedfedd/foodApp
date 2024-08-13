package com.foodApp.controller;

import com.foodApp.model.Restaurant;
import com.foodApp.model.User;
import com.foodApp.response.MessagResponse;
import com.foodApp.service.CartService;
import com.foodApp.service.RestaurantService;
import com.foodApp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class SuperAdminController {

    @Autowired
    private UserService userService;

    @Autowired
    private RestaurantService restaurantService;
    @Autowired
    private CartService cartService;

    @GetMapping("/api/customers")
    public ResponseEntity<List<User>> getAllCustomers() {

        List<User> users =userService.findAllUsers();

        return new ResponseEntity<>(users,HttpStatus.ACCEPTED);

    }

    @DeleteMapping("/api/customers/{id}")
    public ResponseEntity<MessagResponse> deleteCustomer(
            @RequestHeader("Authorization") String jwt,
            @PathVariable Long id
    ) throws Exception {
        User user = userService.findUserByJwtToken(jwt);
        userService.deleteUserById(id);

        MessagResponse res = new MessagResponse();
        res.setMessage("Customer deleted successfully");
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @DeleteMapping("/api/restaurants/{id}")
    public ResponseEntity<MessagResponse> deleteRestaurant(
            @RequestHeader("Authorization") String jwt,
            @PathVariable Long id
    ) throws Exception {
        // Delete related cart entries
        cartService.deleteCartEntriesByRestaurantId(id);

        // Delete the restaurant by ID
        restaurantService.deleteRestaurant(id);

        // Create a response message
        MessagResponse res = new MessagResponse();
        res.setMessage("Restaurant and related cart entries deleted successfully");

        return new ResponseEntity<>(res, HttpStatus.OK);
    }

}
