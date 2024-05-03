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
    private final MizDooni mizDooniService = MizDooni.getInstance();

    // Endpoint to fetch all users
//    @GetMapping("/users")
//    public ArrayList<User> getAllUsers() {
//        return mizDooniService.getUsers();
//    }

    @GetMapping("/users")
    public ResponseEntity<User> getUserByUsername(@RequestParam(required = false) String username) {
        // Use `frontendUsername` if provided, otherwise use `username` from the path
        String userToSearch = username;
        User user = mizDooniService.getUserByUsername(userToSearch);
        ResponseEntity<User> responseEntity;
        if (user != null) {
            responseEntity = ResponseEntity.ok(user);
            System.out.println("ResponseEntity: " + responseEntity);
        } else {
            responseEntity = ResponseEntity.notFound().build();
            System.out.println("ResponseEntity: " + responseEntity);
        }

        // Print the ResponseEntity object to the console
        System.out.println("ResponseEntity: " + responseEntity);

        return responseEntity;
    }

//    @GetMapping("/users/{username}")
//    public ResponseEntity<User> getUserByUsername(@PathVariable String username) {
//        User user = mizDooniService.getUserByUsername(username);
//        System.out.println("Here");
//        if (user != null) {
//            return ResponseEntity.ok(user);
//        } else {
//            return ResponseEntity.notFound().build();
//        }
//    }

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

    @GetMapping("/typesCountriesAndCities")
    public ResponseEntity<Map<String, Map<String, List<String>>>> getTypesCountriesAndCities() {
        try {
            Map<String, Map<String, List<String>>> typeCountryCityMap = mizDooniService.getTypesCountriesAndCities();
            return ResponseEntity.ok().body(typeCountryCityMap);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<User> login(
            @RequestParam String username,
            @RequestParam String password
    ) {
        try {
            User validatedUser = mizDooniService.login(username, password);
            return ResponseEntity.ok().body(validatedUser);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
//
//    @PostMapping("/signup")
//    public ResponseEntity<String> signUp(
//            @RequestParam String username,
//            @RequestParam String password,
//            @RequestParam String email,
//            @RequestParam String role,
//            @RequestParam String city,
//            @RequestParam String country
//    ) {
//        try {
//            mizDooniService.signUp(username, password, email, role, city, country);
//            return ResponseEntity.ok().body("User registered successfully");
//        } catch (Exception e) {
//            return ResponseEntity.badRequest().body(null);
//        }
//    }
}

