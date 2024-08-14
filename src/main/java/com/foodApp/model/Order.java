package com.foodApp.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.foodApp.model.Address;
import com.foodApp.model.OrderItem;
import com.foodApp.model.Restaurant;
import com.foodApp.model.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "orders")
public class Order {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	private User customer;

	@JsonIgnore
	@ManyToOne
	private Restaurant restaurant;

	private Long totalAmount;

	private String orderStatus;

	@Temporal(TemporalType.TIMESTAMP)
	private Date createdAt;

	@ManyToOne
	private Address deliveryAddress;

	//	@JsonIgnore
	@OneToMany
	private List<OrderItem> items;


	private Long totalItem;

	private Long totalPrice;

}


