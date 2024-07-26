package com.foodApp.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.foodApp.model.Cart;

public interface CartRepository extends JpaRepository<Cart, Long> {


}
