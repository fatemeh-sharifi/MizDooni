package Routers;

import Entity.Address.AddressRestaurantEntity;
import Entity.Restaurant.RestaurantEntity;
import Repository.Address.AddressRestaurantRepository;
import Repository.Restaurant.RestaurantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

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
    @GetMapping("/types")
    public ResponseEntity<List<String>> findRestaurantsTypes(){
        try{
            List<String> types = restaurantRepository.findDistinctTypes();
            return ResponseEntity.ok().body(types);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
    @Autowired
    private AddressRestaurantRepository addressRestaurantRepository;

    public Map<String, Set<String>> getCountriesAndCities() {
        List<AddressRestaurantEntity> addresses = addressRestaurantRepository.findAll();
        Map<String, Set<String>> countryCityMap = new HashMap<>();

        for (AddressRestaurantEntity address : addresses) {
            String country = address.getCountry();
            String city = address.getCity();

            countryCityMap.computeIfAbsent(country, k -> new HashSet<>()).add(city);
        }

        return countryCityMap;
    }
    @GetMapping("/location")
    public ResponseEntity<Map<String, Set<String>>> findRestaurantsLocation() {
        try {
            Map<String, Set<String>> countryCityMap = getCountriesAndCities();
            return ResponseEntity.ok().body(countryCityMap);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }

}
}
