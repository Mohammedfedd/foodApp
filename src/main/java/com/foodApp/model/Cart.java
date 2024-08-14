package com.foodApp.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Cart {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY) // Fetch type LAZY is often preferred for performance
	@JoinColumn(name = "customer_id", nullable = false)
	private User customer;

	// Ensure total is not null
	private Long total;

	@OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<CartItem> items = new ArrayList<>();

	@ManyToOne(fetch = FetchType.LAZY) // Fetch type LAZY to avoid unnecessary data loading
	@JoinColumn(name = "restaurant_id")
	private Restaurant restaurant;
}
