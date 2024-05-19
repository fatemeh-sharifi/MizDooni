package Controller.User;

import Entity.Address.AddressUserEntity;
import Entity.User.ClientEntity;
import Entity.User.ManagerEntity;
import Entity.User.UserEntity;
import Repository.User.UserRepository;
import Service.User.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;

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

//    @PostMapping("/login")
//    public ResponseEntity<UserEntity> login(
//            @RequestParam String username,
//            @RequestParam String password
//    ) {
//        try {
//            // Query the database to find the user based on the username and password
//            String hashedPassword = userService.hashPassword(password);
//            System.out.println (hashedPassword);
//            UserEntity user = userRepository.findByUsername(username);
//            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
//            boolean isPasswordCorrect = encoder.matches(password, hashedPassword);
//            System.out.println (hashedPassword);
//            System.out.println (isPasswordCorrect );
//            if (user != null & isPasswordCorrect) {
//                // Return the user if found
//                return ResponseEntity.ok().body(user);
//            } else {
//                // Return 404 Not Found if user is not found
//                return ResponseEntity.notFound().build();
//            }
//        } catch (Exception e) {
//            // Return 500 Internal Server Error if an error occurs
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
//        }
//    }
    @PostMapping("/signup")
    public ResponseEntity<UserEntity> signIn(
            @RequestParam String username,
            @RequestParam String password,
            @RequestParam String email,
            @RequestParam String role,
            @RequestParam String city,
            @RequestParam String country
    ) {
        try {
            // Check if the username is already taken
            if (userRepository.existsByUsername(username)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null); // Username already exists, return bad request
            }

            AddressUserEntity addressUser = new AddressUserEntity();
            addressUser.setCity(city);
            addressUser.setCountry(country);

            UserEntity newUser;
            if (role.equalsIgnoreCase("manager")) {
                String hashed_password = userService.hashPassword(password);
                newUser = new ManagerEntity(username, email, hashed_password, role, addressUser);
            } else if (role.equalsIgnoreCase("client")) {
                String hashed_password = userService.hashPassword(password);
                newUser = new ClientEntity(username, email, hashed_password, role, addressUser);
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null); // Invalid role, return bad request
            }

            userRepository.save(newUser);
            return ResponseEntity.ok(newUser); // Return the newly created user
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null); // Failed to create user, return bad request
        }
    }
}