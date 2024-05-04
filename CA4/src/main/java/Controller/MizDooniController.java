package Controller;

import Model.Exception.SuperException;
import Model.Feedback.Feedback;
import Model.Restaurant.Restaurant;
import Model.User.User;
import Service.MizDooni;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/")
public class MizDooniController {

    private final MizDooni mizDooniService = MizDooni.getInstance();


    @GetMapping("/users")
    public ArrayList<User> getAllUsers() {
        return mizDooniService.getUsers();
    }
    @GetMapping("/users/{username}")
    public ResponseEntity<User> getUserByUsername(@PathVariable String username) {
        User user = mizDooniService.getUserByUsername(username);
        System.out.println("user");

        ResponseEntity <User>responseEntity;
        if (user == null) {
            responseEntity = ResponseEntity.notFound().build();
        }
        else {
            responseEntity = ResponseEntity.ok(user);
        }
        System.out.println(responseEntity);
        return responseEntity;
    }

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
    @GetMapping("/restaurants/{id}")
    public ResponseEntity<Restaurant> findRestaurantById(@PathVariable int id) {
        try {
            // Retrieve restaurant by ID
            Restaurant restaurant = mizDooniService.getRestaurantById(id);

            // Check if the restaurant exists
            if (restaurant != null) {
                return ResponseEntity.ok().body(restaurant);
            } else {
                return ResponseEntity.notFound().build(); // Return 404 if restaurant not found
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null); // Return 400 if there's an error
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

@PostMapping("/reviews")
public ResponseEntity<String> addOrUpdateReview(
        @RequestParam(required = false) String username,
        @RequestParam(required = false) String restaurantName,
        @RequestParam(required = false) Double foodRate,
        @RequestParam(required = false) Double serviceRate,
        @RequestParam(required = false) Double ambianceRate,
        @RequestParam(required = false) Double overallRate,
        @RequestParam(required = false) String comment
) {
    try {
        // Check if username and restaurantName are provided
        if (username == null || restaurantName == null) {
            return ResponseEntity.status(400).body("Username and restaurantName are required parameters");
        }

        // Retrieve user and restaurant
        User user = mizDooniService.getUserByUsername(username);
        Restaurant restaurant = mizDooniService.getRestaurantByName(restaurantName);

        // If either user or restaurant not found, return bad request
        if (user == null || restaurant == null) {
            return ResponseEntity.status(400).body("User or restaurant not found");
        }

        // If other parameters are provided, proceed with review update
        if (foodRate != null && serviceRate != null && ambianceRate != null && overallRate != null && comment != null) {
            if (!mizDooniService.isReservationTimePassed(user, restaurant)) {
                return ResponseEntity.status(400).body("You need to have a past reservation to post a review");
            }

            // Create or update feedback
            Feedback feedback = new Feedback(username, restaurantName, foodRate, serviceRate, ambianceRate, overallRate, comment, LocalDateTime.now());
            mizDooniService.updateFeedback(feedback);

            // Update restaurant ratings
            mizDooniService.updateRestaurantRatings(restaurantName, foodRate, serviceRate, ambianceRate, overallRate);

            // Update user feedbacks
            user.getFeedbacks().add(feedback);

            return ResponseEntity.ok("Review added/updated successfully");
        } else {
            // If only username and restaurantName are provided, return 200 indicating it's allowed
            return ResponseEntity.ok("Review parameters are valid, but review update not requested");
        }
    } catch (Exception e) {
        return ResponseEntity.status(400).body("Failed to add/update review: " + e.getMessage());
    }
}

}

