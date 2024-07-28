package com.foodApp.request;

import com.foodApp.model.Address;
import lombok.Data;

@Data
public class OrderRequest {
    private Long restaurantId;
    private Address deliveryAddress;
}
