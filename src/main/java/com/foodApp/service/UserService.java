package com.foodApp.service;

import com.foodApp.model.User;

import java.util.List;

public interface UserService {

    public User findUserByJwtToken(String jwt) throws Exception;

    public User findUserByEmail(String email) throws Exception;
    public List<User> findAllUsers();
    public List<User> getPenddingRestaurantOwner();


}
