package com.zosh.service;

import com.zosh.model.ArchivedRestaurant;

import java.util.List;

public interface ArchivedRestaurantService {
    List<ArchivedRestaurant> getAllArchivedRestaurants();
    ArchivedRestaurant getArchivedRestaurantById(Long id);
    void unarchiveRestaurant(Long id);
}