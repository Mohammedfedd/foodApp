package com.foodApp.dto;

import com.foodApp.model.Address;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;

import java.util.List;

@Data
@Embeddable
public class RestaurantDto {
	private String title;
	@Column(length = 1000)
	private List<String> images;
	private String description;
	private Long id;
	private boolean open; // If you want to include this

	// Address fields
	private String streetAddress;
	private String city;
	private String stateProvince;
	private String postalCode;
	private String country;
}
