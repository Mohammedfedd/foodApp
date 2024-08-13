package com.foodApp.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Address {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	private String streetAddress;
	private String city;
	private String stateProvince;
	private String postalCode;
	private String country;
}
