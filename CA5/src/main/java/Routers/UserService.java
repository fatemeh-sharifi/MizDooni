package Routers;

import Entity.User.UserEntity;
import Repository.User.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/users")
    public ResponseEntity<List<UserEntity>> getAllUsers() {
        List<UserEntity> users = userRepository.findAll();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/users/{username}")
    public ResponseEntity<UserEntity> getUserByUsername(@PathVariable String username) {
        UserEntity user = userRepository.findByUsername(username);
        if (user == null) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(user);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<UserEntity> login(
            @RequestParam String username,
            @RequestParam String password
    ) {
        try {
            // Query the database to find the user based on the username and password
            UserEntity user = userRepository.findByUsernameAndPassword(username, password);

            if (user != null) {
                // Return the user if found
                return ResponseEntity.ok().body(user);
            } else {
                // Return 404 Not Found if user is not found
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            // Return 500 Internal Server Error if an error occurs
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
