package Controller.User;

import Entity.Address.AddressUserEntity;
import Entity.User.ClientEntity;
import Entity.User.ManagerEntity;
import Entity.User.UserEntity;
import Repository.User.UserRepository;
import Service.User.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import co.elastic.apm.api.ElasticApm;
import co.elastic.apm.api.Scope;
import co.elastic.apm.api.Span;
import co.elastic.apm.api.Transaction;
import java.util.List;

@RestController
public class UserController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;
//    @GetMapping("/users")
//    public ResponseEntity<List<UserEntity>> getAllUsers() {
//        List<UserEntity> users = userRepository.findAll();
//        return ResponseEntity.ok(users);
//    }
//
//    @GetMapping("/users/{username}")
//    public ResponseEntity<UserEntity> getUserByUsername(@PathVariable String username) {
//        UserEntity user = userRepository.findByUsername(username);
//        if (user == null) {
//            return ResponseEntity.notFound().build();
//        } else {
//            return ResponseEntity.ok(user);
//        }
//    }
//
//    @PostMapping("/signup")
//    public ResponseEntity<UserEntity> signIn(
//            @RequestParam String username,
//            @RequestParam String password,
//            @RequestParam String email,
//            @RequestParam String role,
//            @RequestParam String city,
//            @RequestParam String country
//    ) {
//        try {
//            // Check if the username is already taken
//            if (userRepository.existsByUsername(username)) {
//                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null); // Username already exists, return bad request
//            }
//
//            AddressUserEntity addressUser = new AddressUserEntity();
//            addressUser.setCity(city);
//            addressUser.setCountry(country);
//
//            UserEntity newUser;
//            if (role.equalsIgnoreCase("manager")) {
//                String hashed_password = userService.hashPassword(password);
//                newUser = new ManagerEntity(username, email, hashed_password, role, addressUser);
//            } else if (role.equalsIgnoreCase("client")) {
//                String hashed_password = userService.hashPassword(password);
//                newUser = new ClientEntity(username, email, hashed_password, role, addressUser);
//            } else {
//                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null); // Invalid role, return bad request
//            }
//
//            userRepository.save(newUser);
//            return ResponseEntity.ok(newUser); // Return the newly created user
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null); // Failed to create user, return bad request
//        }
//    }
    @GetMapping("/users")
    public ResponseEntity<List<UserEntity>> getAllUsers() {
        Transaction transaction = ElasticApm.startTransaction();
        try (Scope txScope = transaction.activate()) {
            transaction.setName("HTTP GET /users");
            transaction.setType(Transaction.TYPE_REQUEST);

            Span dbSpan = transaction.startSpan("db", "query", "findAllUsers");
            try (Scope dbScope = dbSpan.activate()) {
                dbSpan.setName("Find All Users");

                List<UserEntity> users = userRepository.findAll();
                dbSpan.end();

                return ResponseEntity.ok(users);
            } catch (Exception e) {
                dbSpan.captureException(e);
                dbSpan.end();
                throw e;
            }
        } catch (Exception e) {
            transaction.captureException(e);
            return ResponseEntity.badRequest().body(null);
        } finally {
            transaction.end();
        }
    }

    @GetMapping("/users/{username}")
    public ResponseEntity<UserEntity> getUserByUsername(@PathVariable String username) {
        Transaction transaction = ElasticApm.startTransaction();
        try (Scope txScope = transaction.activate()) {
            transaction.setName("HTTP GET /users/{username}");
            transaction.setType(Transaction.TYPE_REQUEST);

            Span dbSpan = transaction.startSpan("db", "query", "findUserByUsername");
            try (Scope dbScope = dbSpan.activate()) {
                dbSpan.setName("Find User By Username");

                UserEntity user = userRepository.findByUsername(username);
                dbSpan.end();

                if (user == null) {
                    return ResponseEntity.notFound().build();
                } else {
                    return ResponseEntity.ok(user);
                }
            } catch (Exception e) {
                dbSpan.captureException(e);
                dbSpan.end();
                throw e;
            }
        } catch (Exception e) {
            transaction.captureException(e);
            return ResponseEntity.badRequest().body(null);
        } finally {
            transaction.end();
        }
    }

    @PostMapping("/signup")
    public ResponseEntity<UserEntity> signIn(
            @RequestParam String username,
            @RequestParam String password,
            @RequestParam String email,
            @RequestParam String role,
            @RequestParam String city,
            @RequestParam String country
    ) {
        Transaction transaction = ElasticApm.startTransaction();
        try (Scope txScope = transaction.activate()) {
            transaction.setName("HTTP POST /signup");
            transaction.setType(Transaction.TYPE_REQUEST);

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

            Span dbSpan = transaction.startSpan("db", "query", "saveUser");
            try (Scope dbScope = dbSpan.activate()) {
                dbSpan.setName("Save User");

                userRepository.save(newUser);
                dbSpan.end();

                return ResponseEntity.ok(newUser); // Return the newly created user
            } catch (Exception e) {
                dbSpan.captureException(e);
                dbSpan.end();
                throw e;
            }
        } catch (Exception e) {
            transaction.captureException(e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null); // Failed to create user, return bad request
        } finally {
            transaction.end();
        }
    }
}