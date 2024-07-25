package com.artiweb.foodApp.repositories;

import com.artiweb.foodApp.entities.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Long> {

}
