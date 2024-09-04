package com.zosh.repository;

import com.zosh.model.ArchivedRestaurant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArchivedRestaurantRepository extends JpaRepository<ArchivedRestaurant, Long> {
    // Custom query methods if needed
}