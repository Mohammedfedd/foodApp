package com.foodApp.repository;

import com.foodApp.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderitemRepository extends JpaRepository<OrderItem,Long> {
}
