package DAO.Restaurant;

import Entity.Restaurant.RestaurantEntity;
import Repository.Restaurant.RestaurantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RestaurantDAO {
    private final RestaurantRepository restaurantRepository;

    @Autowired
    public RestaurantDAO(RestaurantRepository restaurantRepository) {
        this.restaurantRepository = restaurantRepository;
    }

    public List<RestaurantEntity> findRestaurants(String type, String city, String country, String name) {
        return restaurantRepository.findByFilters(type, city, country, name);
    }
}
