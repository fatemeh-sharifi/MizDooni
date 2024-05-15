package Routers;

import Entity.Restaurant.RestaurantEntity;
import Repository.Restaurant.RestaurantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class RestaurantService {


    @Autowired
    RestaurantRepository restaurantRepository;

    @GetMapping("/restaurants")
    public ResponseEntity<List<RestaurantEntity>> findRestaurants (
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String country,
            @RequestParam(required = false) String name
    ) {
        try {
            List<RestaurantEntity> filteredRestaurants = restaurantRepository.findRestaurants(type, city, country, name);
            return ResponseEntity.ok().body(filteredRestaurants);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/restaurants/{id}")
    public ResponseEntity<RestaurantEntity> findRestaurantById ( @PathVariable int id ) {
        try {
            RestaurantEntity restaurant = restaurantRepository.findRestaurantById(id);
            if (restaurant != null) {
                return ResponseEntity.ok().body(restaurant);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/topRestaurants")
    public ResponseEntity<List<RestaurantEntity>> findTopRestaurants (
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String country ) {
        try {
            List<RestaurantEntity> topRestaurants = restaurantRepository.findTopRestaurants(username, type, city, country);
            return ResponseEntity.ok().body(topRestaurants);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
}
