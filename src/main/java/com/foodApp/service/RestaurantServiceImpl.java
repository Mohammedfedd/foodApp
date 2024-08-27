package com.foodApp.service;

import com.foodApp.dto.RestaurantDto;
import com.foodApp.model.*;
import com.foodApp.repository.AddressRepository;
import com.foodApp.repository.ArchivedRestaurantRepository;
import com.foodApp.repository.RestaurantRepository;
import com.foodApp.repository.UserRepository;
import com.foodApp.request.CreateRestaurantRequest;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class RestaurantServiceImpl implements RestaurantService {
    @Autowired
    RestaurantRepository restaurantRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CartService cartService;
    @Autowired
    private ArchivedRestaurantRepository archivedRestaurantRepository;
    @Override
    public Restaurant createRestaurant(CreateRestaurantRequest req, User user) {
        Address address=addressRepository.save(req.getAddress());
        Restaurant restaurant = new Restaurant();
        restaurant.setAddress(address);
        restaurant.setContactInformation(req.getContactInformation());
        restaurant.setCuisineType(req.getCuisineType());
        restaurant.setDescription(req.getDescription());
        restaurant.setImages(req.getImages());
        restaurant.setName(req.getName());
        restaurant.setOpeningHours(req.getOpeningHours());
        restaurant.setRegistrationDate(LocalDateTime.now());
        restaurant.setOwner(user);

        return restaurantRepository.save(restaurant);
    }

    @Override
    public Restaurant updateRestaurant(Long restaurantId, CreateRestaurantRequest updatedRestaurant) throws Exception {
        Restaurant restaurant=findRestaurantById(restaurantId);
        if(restaurant.getCuisineType()!=null)
        {
            restaurant.setCuisineType(updatedRestaurant.getCuisineType());
        }
        if(restaurant.getDescription()!=null)
        {
            restaurant.setDescription(updatedRestaurant.getDescription());
        }
        if(restaurant.getName()!=null)
        {
            restaurant.setName(updatedRestaurant.getName());
        }
        return restaurantRepository.save(restaurant);
    }

    @Override
    public void deleteRestaurant(Long restaurantId) throws Exception {
        Restaurant restaurant=findRestaurantById(restaurantId);
        restaurantRepository.delete(restaurant);
    }


    @Override
    public List<Restaurant> getAllRestaurant() {
        return restaurantRepository.findAll();
    }


    @Override
    public List<Restaurant> searchRestaurant(String keyword) {
        return restaurantRepository.findBySearchQuery(keyword);
    }

    @Override
    public Restaurant findRestaurantById(Long restaurantId) throws Exception {
        Optional<Restaurant> opt=restaurantRepository.findById(restaurantId);
        if(opt.isEmpty())
        {
            throw new Exception("Restaurant not found");
        }
        return opt.get();
    }

    @Override
    public Restaurant getRestaurantsByUserId(Long userId) throws Exception {
        Restaurant restaurants=restaurantRepository.findByOwnerId(userId);
        return restaurants;
    }

    @Override
    public RestaurantDto addToFavorite(Long restaurantId, User user) throws Exception {
        Restaurant restaurant = findRestaurantById(restaurantId);
        RestaurantDto dto = new RestaurantDto();
        dto.setDescription(restaurant.getDescription());
        dto.setImages(restaurant.getImages());
        dto.setTitle(restaurant.getName());
        dto.setId(restaurantId);
        dto.setOpen(restaurant.isOpen());  // Set the open status

        // Map address fields
        Address address = restaurant.getAddress(); // Assuming getAddress() returns an Address object
        if (address != null) {
            dto.setStreetAddress(address.getStreetAddress());
            dto.setCity(address.getCity());
            dto.setStateProvince(address.getStateProvince());
            dto.setPostalCode(address.getPostalCode());
            dto.setCountry(address.getCountry());
        }

        boolean isFavorited = false;
        List<RestaurantDto> favorites = user.getFavorites();
        for (RestaurantDto favorite : favorites) {
            if (favorite.getId().equals(restaurantId)) {
                isFavorited = true;
                break;
            }
        }
        if (isFavorited) {
            favorites.removeIf(favorite -> favorite.getId().equals(restaurantId));
        } else {
            favorites.add(dto);
        }

        userRepository.save(user);
        return dto;
    }
    @Override
    public void markRestaurantAsDiscontinued(Long id) throws Exception {
        Restaurant restaurant = restaurantRepository.findById(id)
                .orElseThrow(() -> new Exception("Restaurant not found"));
        restaurant.setDiscontinued(true);
        restaurantRepository.save(restaurant);
    }
    @Override
    public void archiveRestaurant(Long restaurantId) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new EntityNotFoundException("Restaurant not found"));

        // Set status to archived
        restaurant.setStatus(RestaurantStatus.ARCHIVED);

        // Optionally clear ingredient categories if required
        // restaurant.setIngredientCategories(new ArrayList<>());

        restaurantRepository.save(restaurant);
    }

    @Override
    public void unarchiveRestaurant(Long restaurantId) throws Exception {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new EntityNotFoundException("Restaurant not found"));
        restaurant.setStatus(RestaurantStatus.ACTIVE);
        restaurantRepository.save(restaurant);

    }

    @Override
    public void discontinueRestaurant(Long id, String reason) throws Exception {
        Restaurant restaurant = restaurantRepository.findById(id)
                .orElseThrow(() -> new Exception("Restaurant not found"));
        restaurant.setDiscontinued(true);
        restaurant.setDiscontinuationDate(LocalDateTime.now());
        restaurant.setDiscontinuationReason(reason);
        restaurantRepository.save(restaurant);
    }
    @Override
    public Restaurant updateRestaurantStatus(Long id) throws Exception {
        Restaurant restaurant=findRestaurantById(id);
        restaurant.setOpen(!restaurant.isOpen());
        return restaurantRepository.save(restaurant);
    }
}
