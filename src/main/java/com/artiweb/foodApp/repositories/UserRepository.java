package com.artiweb.foodApp.repositories;

import com.artiweb.foodApp.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserRepository extends JpaRepository<User,Long> {
    public User findByEmail(String username);

}
