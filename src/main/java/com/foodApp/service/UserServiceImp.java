package com.foodApp.service;

import com.foodApp.config.JwtProvider;
import com.foodApp.model.Cart;
import com.foodApp.model.Restaurant;
import com.foodApp.model.User;
import com.foodApp.repository.CartRepository;
import com.foodApp.repository.RestaurantRepository;
import com.foodApp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImp implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtProvider jwtProvider;

    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private RestaurantRepository restaurantRepository;

    @Override
    public User findUserByJwtToken(String jwt) throws Exception {
        String email=jwtProvider.getEmailFromJwtToken(jwt);
        User user=userRepository.findByEmail(email);
        return user;
    }

    @Override
    public User findUserByEmail(String email) throws Exception {
        User user=userRepository.findByEmail(email);
        if(user==null){
            throw new Exception("User not found");
        }
        return user;
    }

    @Override
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public void deleteUserById(Long userId) {
        // Find the restaurant associated with the user
        Restaurant restaurant = restaurantRepository.findByOwnerId(userId);

        // Delete the restaurant if it exists
        if (restaurant != null) {
            restaurantRepository.delete(restaurant);
        }

        // Delete the cart entries associated with the user
        cartRepository.deleteById(userId);

        // Finally, delete the user
        userRepository.deleteById(userId);
    }



//    @Override
//    public List<User> getPenddingRestaurantOwner() {
//        return List.of();
//    }
}
