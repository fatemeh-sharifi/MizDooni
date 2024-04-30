package Controller;

import Model.Restaurant.Restaurant;
import Model.User.User;
import Service.MizDooni;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

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
    @PostMapping("/users")
    public ResponseEntity<String> createUser( @RequestBody User user) {
        try {
            mizDooniService.addUser(user);
            return ResponseEntity.ok("User created successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to create user: " + e.getMessage());
        }
    }

    @GetMapping("/topRestaurantsBy")
    public ArrayList<Restaurant> getTopRestaurants( @RequestBody User user) { return mizDooniService.findTopRestaurants(user); }
    @GetMapping("/topRestaurantsByOverallAvg")
    public ArrayList<Restaurant> getTopRestaurantsByOverallAvg() { return mizDooniService.findTopRestaurantsByOverallAvg(); }
    @GetMapping("/topRestaurantsByUserLocation")
    public ArrayList<Restaurant> getTopRestaurantsByUserLocation( @RequestBody User user) { return mizDooniService.findTopRestaurantsByUserLocation(user); }
    // Endpoint to update an existing user
//    @PutMapping("/users/{username}")
//    public ResponseEntity<String> updateUser(@PathVariable String username, @RequestBody User user) {
//        try {
//            // Find the user by username and update its properties
//            // You need to implement the updateUser method in your service class
//            mizDooniService.updateUser(username, user);
//            return ResponseEntity.ok("User updated successfully");
//        } catch (Exception e) {
//            return ResponseEntity.badRequest().body("Failed to update user: " + e.getMessage());
//        }
//    }

    // Endpoint to delete a user by username
//    @DeleteMapping("/users/{username}")
//    public ResponseEntity<String> deleteUser(@PathVariable String username) {
//        try {
//            // Delete the user by username
//            // You need to implement the deleteUser method in your service class
//            mizDooniService.deleteUser(username);
//            return ResponseEntity.ok("User deleted successfully");
//        } catch (Exception e) {
//            return ResponseEntity.badRequest().body("Failed to delete user: " + e.getMessage());
//        }
//    }

    // Define more endpoints as needed for other operations
}
