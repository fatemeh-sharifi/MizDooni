package Controller;

import DAO.Restaurant.RestaurantDAO;
import Model.Feedback.Feedback;
import Model.Reservation.Reservation;
import Model.Restaurant.Restaurant;
import Model.Table.Table;
import Model.User.User;
import Repository.User.UserRepository;
import Response.AvailableTimeResponse;
import Service.Mizdooni.MizDooni;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/")
public class MizDooniController {

    private final MizDooni mizDooniService = MizDooni.getInstance();

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RestaurantDAO restaurantDAO;

//    @GetMapping("/users")
//    public ResponseEntity<List<Entity.User.UserEntity>> getAllUsers() {
//        List<Entity.User.UserEntity> users = userRepository.findAll();
//        return ResponseEntity.ok(users);
//    }
//
//    @GetMapping("/users/{username}")
//    public ResponseEntity<Entity.User.UserEntity> getUserByUsername( @PathVariable String username) {
//        Entity.User.UserEntity user = userRepository.findByUsername(username);
//        if (user == null) {
//            return ResponseEntity.notFound().build();
//        } else {
//            return ResponseEntity.ok(user);
//        }
//    }

//    @GetMapping("/users")
//    public ArrayList<User> getAllUsers() {
//        return mizDooniService.getUsers();
//    }
//    @GetMapping("/users/{username}")
//    public ResponseEntity<User> getUserByUsername(@PathVariable String username) {
//        User user = mizDooniService.getUserByUsername(username);
//        System.out.println("user");
//
//        ResponseEntity <User>responseEntity;
//        if (user == null) {
//            responseEntity = ResponseEntity.notFound().build();
//        }
//        else {
//            responseEntity = ResponseEntity.ok(user);
//        }
//        System.out.println(responseEntity);
//        return responseEntity;
//    }

    @GetMapping("/restaurants/by_type/{type_name}")
    public ResponseEntity<List<Restaurant>> findRestaurants_by_type(
            @PathVariable(required = false) String type_name,
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String country,
            @RequestParam(required = false) String name
    ) {
        try {
            System.out.println(type_name);
            List<Restaurant> allRestaurants = mizDooniService.getRestaurants();
            List<Restaurant> filteredRestaurants = allRestaurants.stream()
                    .filter(restaurant -> type_name == null || restaurant.getType().equalsIgnoreCase(type_name))
                    .filter(restaurant -> city == null || restaurant.getAddress().getCity().equalsIgnoreCase(city))
                    .filter(restaurant -> country == null || restaurant.getAddress().getCountry().equalsIgnoreCase(country))
                    .filter(restaurant -> name == null || restaurant.getName().equalsIgnoreCase(name))
                    .collect(Collectors.toList());

            return ResponseEntity.ok().body(filteredRestaurants);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

//    @GetMapping("/restaurants")
//    public ResponseEntity<List<Restaurant>> findRestaurants(
//            @RequestParam(required = false) String type,
//            @RequestParam(required = false) String city,
//            @RequestParam(required = false) String country,
//            @RequestParam(required = false) String name
//    ) {
//        try {
//            List<Restaurant> allRestaurants = mizDooniService.getRestaurants();
//            List<Restaurant> filteredRestaurants = allRestaurants.stream()
//                    .filter(restaurant -> type == null || restaurant.getType().equalsIgnoreCase(type))
//                    .filter(restaurant -> city == null || restaurant.getAddress().getCity().equalsIgnoreCase(city))
//                    .filter(restaurant -> country == null || restaurant.getAddress().getCountry().equalsIgnoreCase(country))
//                    .filter(restaurant -> name == null || restaurant.getName().equalsIgnoreCase(name))
//                    .collect(Collectors.toList());
//            return ResponseEntity.ok().body(filteredRestaurants);
//        } catch (Exception e) {
//            return ResponseEntity.badRequest().body(null);
//        }
//    }

//    @GetMapping("/restaurants")
//    public ResponseEntity<List<RestaurantEntity>> findRestaurants(
//            @RequestParam(required = false) String type,
//            @RequestParam(required = false) String city,
//            @RequestParam(required = false) String country,
//            @RequestParam(required = false) String name
//    ) {
//        try {
//            List<RestaurantEntity> filteredRestaurants = restaurantDAO.findRestaurants(type, city, country, name);
//            return ResponseEntity.ok().body(filteredRestaurants);
//        } catch (Exception e) {
//            return ResponseEntity.badRequest().body(null);
//        }
//    }
//    @GetMapping("/restaurants/{id}")
//    public ResponseEntity<Restaurant> findRestaurantById(@PathVariable int id) {
//        try {
//            // Retrieve restaurant by ID
//            Restaurant restaurant = mizDooniService.getRestaurantById(id);
//
//            // Check if the restaurant exists
//            if (restaurant != null) {
//                return ResponseEntity.ok().body(restaurant);
//            } else {
//                return ResponseEntity.notFound().build(); // Return 404 if restaurant not found
//            }
//        } catch (Exception e) {
//            return ResponseEntity.badRequest().body(null); // Return 400 if there's an error
//        }
//    }

//    @GetMapping("/topRestaurants")
//    public ResponseEntity<List<Restaurant>> findTopRestaurants(
//            @RequestParam(required = false) String username,
//            @RequestParam(required = false) String type,
//            @RequestParam(required = false) String city,
//            @RequestParam(required = false) String country
//    ) {
//        try {
//            List<Restaurant> allRestaurants = mizDooniService.getRestaurants();
//
//            // If username is provided, filter restaurants by user's city and country
//            if (username != null) {
//                User user = mizDooniService.getUserByUsername(username);
//                city = user.getAddress().getCity();
//                country = user.getAddress().getCountry();
//            }
//
//            String finalCity = city;
//            String finalCountry = country;
//            List<Restaurant> filteredRestaurants = allRestaurants.stream()
//                    .filter(restaurant -> type == null || restaurant.getType().equalsIgnoreCase(type))
//                    .filter(restaurant -> finalCity == null || restaurant.getAddress().getCity().equalsIgnoreCase(finalCity))
//                    .filter(restaurant -> finalCountry == null || restaurant.getAddress().getCountry().equalsIgnoreCase(finalCountry))
//                    .collect(Collectors.toList());
//
//            filteredRestaurants.sort(Comparator.comparingDouble(Restaurant::getOverallAvg).reversed());
//            List<Restaurant> topRestaurants = filteredRestaurants.stream().limit(6).collect(Collectors.toList());
//
//            return ResponseEntity.ok().body(topRestaurants);
//        } catch (Exception e) {
//            return ResponseEntity.badRequest().body(null);
//        }
//    }
//    @GetMapping("/types")
//    public ResponseEntity<List<String>> findRestaurantsTypes(){
//        try{
//            return ResponseEntity.ok().body(mizDooniService.getAllRestaurantTypes());
//        }catch (Exception e) {
//            return ResponseEntity.badRequest().body(null);
//        }
//    }
//    @GetMapping("/location")
//    public ResponseEntity<Map<String, List<String>>> findRestaurantsLocation() {
//        try {
//            Map<String, List<String>> countryCityMap = mizDooniService.getCountriesAndCities();
//            return ResponseEntity.ok().body(countryCityMap);
//        } catch (Exception e) {
//            return ResponseEntity.badRequest().body(null);
//        }
//    }

//    @GetMapping("/typesCountriesAndCities")
//    public ResponseEntity<Map<String, Map<String, List<String>>>> getTypesCountriesAndCities() {
//        try {
//            Map<String, Map<String, List<String>>> typeCountryCityMap = mizDooniService.getTypesCountriesAndCities();
//            return ResponseEntity.ok().body(typeCountryCityMap);
//        } catch (Exception e) {
//            return ResponseEntity.badRequest().body(null);
//        }
//    }

//    @PostMapping("/login")
//    public ResponseEntity<User> login(
//            @RequestParam String username,
//            @RequestParam String password
//    ) {
//        try {
//            User validatedUser = mizDooniService.login(username, password);
//            return ResponseEntity.ok().body(validatedUser);
//        } catch (Exception e) {
//            return ResponseEntity.badRequest().body(null);
//        }
//    }

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

        // Retrieve user and restaurant
        User user = mizDooniService.getUserByUsername(username);
        Restaurant restaurant = mizDooniService.getRestaurantByName(restaurantName);


        // If either user or restaurant not found, return bad request
        if (user == null || restaurant == null) {
            return ResponseEntity.status(400).body("User or restaurant not found");
        }

        // If other parameters are provided, proceed with review update
        if (foodRate != null && serviceRate != null && ambianceRate != null && overallRate != null && comment != null) {
            if (!mizDooniService.isReservationTimePassed(username, restaurantName)) {
                return ResponseEntity.status(400).body("You need to have a past reservation to post a review");
            }


            // Create or update feedback
            Feedback feedback = new Feedback(username, restaurantName, foodRate, serviceRate, ambianceRate, overallRate, comment, LocalDateTime.now());
            for(Feedback feedback1: restaurant.getFeedbacks()){
                System.out.println(feedback1);
                if(feedback1.getUsername().equals(feedback.getUsername())){
                    //retract
                    mizDooniService.retractReview(feedback1,foodRate, serviceRate, ambianceRate, overallRate, comment);
                    break;
                }
            }
            mizDooniService.updateFeedback(feedback);

            // Update restaurant ratings

            // Update user feedbacks
            System.out.println(restaurant.getFeedbacks());
            user.getFeedbacks().add(feedback);
            restaurant.getFeedbacks().add(feedback);
            mizDooniService.updateUsers(user);
            mizDooniService.updateRestaurants(restaurant);
            System.out.println(restaurant.getFeedbacks());

            mizDooniService.updateRestaurantRatings(restaurantName, foodRate, serviceRate, ambianceRate, overallRate);
            return ResponseEntity.ok("Review added/updated successfully");
        } else {
            // If only username and restaurantName are provided, return 200 indicating it's allowed
            return ResponseEntity.ok("Review parameters are valid, but review update not requested");
        }
    } catch (Exception e) {
        return ResponseEntity.status(400).body("Failed to add/update review: " + e.getMessage());
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
            // Validate the date
            LocalDate selectedDate = LocalDate.parse(date);
            LocalDate today = LocalDate.now();
            LocalDate maxAllowedDate = today.plusDays(2); // Maximum allowed date is two days after today
            if (selectedDate.isAfter(maxAllowedDate)) {
                return ResponseEntity.badRequest().body(null); // Date exceeds maximum allowed
            }

            Map<String, Object> availability = mizDooniService.getAvailableTimes(restaurantName,numberOfPeople,selectedDate);
            List<Integer> availableTimes = (List<Integer>) availability.get("availableTimes");
            Table availableTable = (Table) availability.get("availableTable");
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

            System.out.println("new Reservation");
            mizDooniService.addReservation(username,restaurantName,tableNumber,date,time);
            return ResponseEntity.ok().body("reservation successfully added.");
        } catch (Exception e) {
            return ResponseEntity.status(400).body("Failed to addreservation: " + e.getMessage());
        }
    }
//    @PostMapping("/signup")
//    public ResponseEntity<User> signUp(
//            @RequestParam String username,
//            @RequestParam String password,
//            @RequestParam String email,
//            @RequestParam String role,
//            @RequestParam String city,
//            @RequestParam String country
//    ) {
//        try {
//            User user =  mizDooniService.signUp(username, password, email, role, city, country);
//            return ResponseEntity.ok().body(user);
//        } catch (Exception e) {
//            System.out.println(e.getMessage());
//            return ResponseEntity.badRequest().body(null);
//        }
//    }

    @GetMapping("/isAble")
    public ResponseEntity<String> isAbleToReview(
            @RequestParam String username,
            @RequestParam String restaurantName
    ) {
        try{
            if (!mizDooniService.isReservationTimePassed(username, restaurantName)) {
                return ResponseEntity.status(400).body("You need to have a past reservation to post a review");
            }
            return ResponseEntity.ok().body("Able to review.");
        } catch (Exception e) {
            return ResponseEntity.status(400).body("Failed: " + e.getMessage());
        }
    }

    @GetMapping("/reservations")
    public ResponseEntity<List<Reservation>> findUserReservations(@RequestParam String username) {
        try {
            List<Reservation> userReservations = mizDooniService.getUserReservations(username);
            return ResponseEntity.ok().body(userReservations);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null); // Return 400 if there's an error
        }
    }

    @PostMapping("/cancelReservation")
    public ResponseEntity<String> cancelReservation(
            @RequestParam String username,
            @RequestParam String restaurantName,
            @RequestParam int tableNumber,
            @RequestParam int reservationNumber
    ) {
        try {
            mizDooniService.cancelReservation(username,restaurantName,tableNumber,reservationNumber);
            return ResponseEntity.ok().body("Canceled successfully");
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.badRequest().body(null);
        }
    }

}

