package Controller;

import Model.Address.AddressUser;
import Model.Feedback.Feedback;
import Model.Reservation.Reservation;
import Model.Restaurant.Restaurant;
import Model.Table.Table;
import Model.User.User;
import Response.AvailableTimeResponse;
import Service.MizDooni;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
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
        @RequestParam String username,
        @RequestParam String restaurantName,
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

    @PostMapping("/signin")
    public ResponseEntity<User> signIn(
            @RequestParam String username,
            @RequestParam String password,
            @RequestParam String email,
            @RequestParam String role,
            @RequestParam String city,
            @RequestParam String country
    ) {
        try {
            // Check if the username is already taken
            if (mizDooniService.getUserByUsername(username) != null) {
                return ResponseEntity.status(400).body(null); // Username already exists, return bad request
            }

            AddressUser addressUser = new AddressUser();
            addressUser.setCity(city);
            addressUser.setCountry(country);
            // Create a new user object
            User newUser = new User(username, email, password, role, addressUser);

            // Add the user to the system
            mizDooniService.addUser(newUser);

            return ResponseEntity.ok(newUser); // Return the newly created user
        } catch (Exception e) {
            return ResponseEntity.status(400).body(null); // Failed to create user, return bad request
        }
    }
    @GetMapping("/tables")
    public ResponseEntity<ArrayList<Table>> tables(){
        return ResponseEntity.ok(mizDooniService.getTables());
    }
    @GetMapping("/availableTimes")
    public ResponseEntity<AvailableTimeResponse> availableTimes(
            @RequestParam String restaurantName,
            @RequestParam int numberOfPeople,
            @RequestParam String date
    ) {
        try {
            System.out.println(date);
            // Validate the date
            LocalDate selectedDate = LocalDate.parse(date);
            System.out.println(selectedDate);
            int year = selectedDate.getYear();
            int month = selectedDate.getMonthValue();
            int day = selectedDate.getDayOfMonth();
            System.out.println(year +" " + month +" " + day);
//            LocalDate selectedDate = selectedDate.toLocalDate();
            LocalDate today = LocalDate.now();
            LocalDate maxAllowedDate = today.plusDays(2); // Maximum allowed date is two days after today
            if (selectedDate.isAfter(maxAllowedDate)) {
                return ResponseEntity.badRequest().body(null); // Date exceeds maximum allowed
            }

            // Retrieve the restaurant
            Restaurant restaurant = mizDooniService.getRestaurantByName(restaurantName);
            if (restaurant == null) {
                return ResponseEntity.notFound().build(); // Restaurant not found
            }

            // Sort tables based on the difference between their capacity and the required number of people
            List<Table> sortedTables = new ArrayList<>(restaurant.getTables());
            sortedTables.sort(Comparator.comparingInt(t -> Math.abs(t.getSeatsNumber() - numberOfPeople)));

            // Find the first available time for the sorted tables
            List<Integer> availableTimes = new ArrayList<>();
            Table availableTable = null;
            for (Table table : sortedTables) {
                List<Integer> tableAvailableTimes = table.getAvailableTimes(selectedDate);
                if (tableAvailableTimes != null && !tableAvailableTimes.isEmpty()) {
                    availableTimes = tableAvailableTimes;
                    availableTable = table;
                    break;
                }
            }

            if (availableTable == null) {
                return ResponseEntity.notFound().build(); // No available times found
            }

            AvailableTimeResponse response = new AvailableTimeResponse(availableTimes, availableTable);
            return ResponseEntity.ok(response); // Return the available time and table
        } catch (DateTimeParseException e) {
            return ResponseEntity.badRequest().body(null); // Invalid date format
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null); // Failed to retrieve available times
        }
    }

    @PostMapping("/addReservation")
    public ResponseEntity<String> reservations(
            @RequestParam String username,
            @RequestParam String restaurantName,
            @RequestParam int tableNumber,
            @RequestParam String date,
            @RequestParam String time
    ) {
        try{
            mizDooniService.addReservation(username,restaurantName,tableNumber,date,time);
            return ResponseEntity.ok().body("reservation successfully added.");
        } catch (Exception e) {
            return ResponseEntity.status(400).body("Failed to add/update review: " + e.getMessage());
        }
    }

}

