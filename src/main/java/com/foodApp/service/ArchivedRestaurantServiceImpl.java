package com.foodApp.service;

import com.foodApp.model.ArchivedRestaurant;
import com.foodApp.repository.ArchivedRestaurantRepository;
import com.foodApp.service.ArchivedRestaurantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ArchivedRestaurantServiceImpl implements ArchivedRestaurantService {

    @Autowired
    private ArchivedRestaurantRepository archivedRestaurantRepository;

    @Override
    public List<ArchivedRestaurant> getAllArchivedRestaurants() {
        return archivedRestaurantRepository.findAll();
    }

    @Override
    public ArchivedRestaurant getArchivedRestaurantById(Long id) {
        return archivedRestaurantRepository.findById(id).orElse(null);
    }

    @Override
    public void unarchiveRestaurant(Long id) {
        ArchivedRestaurant archivedRestaurant = archivedRestaurantRepository.findById(id).orElse(null);
        if (archivedRestaurant != null) {
            // Logic to unarchive (e.g., move to active restaurant list)
            // You might need to delete the archived record and create a new one in the active list
            archivedRestaurantRepository.deleteById(id);
            // Additional logic to move to the active restaurant repository
        }
    }
}
