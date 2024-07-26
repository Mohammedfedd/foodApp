package com.foodApp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.foodApp.model.Address;
public interface AddressRepository extends JpaRepository<Address, Long> {

}
