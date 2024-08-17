package com.foodApp.service;
import com.foodApp.model.ArchivedRestaurant;

import java.util.List;

public interface ArchivedRestaurantService {
    List<ArchivedRestaurant> getAllArchivedRestaurants();
    ArchivedRestaurant getArchivedRestaurantById(Long id);
    void unarchiveRestaurant(Long id);
}
