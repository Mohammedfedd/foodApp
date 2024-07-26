package com.foodApp.request;

import com.foodApp.model.ContactInformation;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class CreateRestaurantRequest {
    private Long id;
    private String name;
    private String description;
    private String cuisineType;
    private String address;
    private ContactInformation contactInformation;
    private String openingHours;
    private List<String> images;

}
