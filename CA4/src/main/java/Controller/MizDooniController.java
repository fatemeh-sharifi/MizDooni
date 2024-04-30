package Controller;

import Model.Restaurant.Restaurant;
import Model.User.User;
import Service.MizDooni;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/")
public class MizDooniController {

//    @Autowired
    private MizDooni mizDooniService = MizDooni.getInstance();

    // Endpoint to fetch all users
    @GetMapping("/users")
    public ArrayList<User> getAllUsers() {
        return mizDooniService.getUsers();
    }

    // Endpoint to fetch a specific user by username
    @GetMapping("/users/{username}")
    public User getUserByUsername(@PathVariable String username) {
        return mizDooniService.getUserByUsername(username);
    }

    // Endpoint to create a new user
    @GetMapping("/restaurants")
    public ResponseEntity<List<Restaurant>> findRestaurants(
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String country,
            @RequestParam(required = false) String name
    ) {
        try {
            List<Restaurant> allRestaurants = mizDooniService.getRestaurants();
            List<Restaurant> filteredRestaurants = allRestaurants.stream()
                    .filter(restaurant -> type == null || restaurant.getType().equalsIgnoreCase(type))
                    .filter(restaurant -> city == null || restaurant.getAddress().getCity().equalsIgnoreCase(city))
                    .filter(restaurant -> country == null || restaurant.getAddress().getCountry().equalsIgnoreCase(country))
                    .filter(restaurant -> name == null || restaurant.getName().equalsIgnoreCase(name))
                    .collect(Collectors.toList());

            return ResponseEntity.ok().body(filteredRestaurants);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/topRestaurants")
    public ResponseEntity<List<Restaurant>> findTopRestaurants(
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String country
    ) {
        try {
            List<Restaurant> allRestaurants = mizDooniService.getRestaurants();

            // If username is provided, filter restaurants by user's city and country
            if (username != null) {
                User user = mizDooniService.getUserByUsername(username);
                city = user.getAddress().getCity();
                country = user.getAddress().getCountry();
            }

            String finalCity = city;
            String finalCountry = country;
            List<Restaurant> filteredRestaurants = allRestaurants.stream()
                    .filter(restaurant -> type == null || restaurant.getType().equalsIgnoreCase(type))
                    .filter(restaurant -> finalCity == null || restaurant.getAddress().getCity().equalsIgnoreCase(finalCity))
                    .filter(restaurant -> finalCountry == null || restaurant.getAddress().getCountry().equalsIgnoreCase(finalCountry))
                    .collect(Collectors.toList());

            filteredRestaurants.sort(Comparator.comparingDouble(Restaurant::getOverallAvg).reversed());
            List<Restaurant> topRestaurants = filteredRestaurants.stream().limit(6).collect(Collectors.toList());

            return ResponseEntity.ok().body(topRestaurants);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
    @GetMapping("/types")
    public ResponseEntity<List<String>> findRestaurantsTypes(){
        try{
            return ResponseEntity.ok().body(mizDooniService.getAllRestaurantTypes());
        }catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
    @GetMapping("/location")
    public ResponseEntity<Map<String, List<String>>> findRestaurantsLocation() {
        try {
            Map<String, List<String>> countryCityMap = mizDooniService.getCountriesAndCities();
            return ResponseEntity.ok().body(countryCityMap);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
}
