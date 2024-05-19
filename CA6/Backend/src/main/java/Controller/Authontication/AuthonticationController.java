package Controller.Authontication;

import Entity.User.UserEntity;
import Repository.User.UserRepository;
import Utility.AuthResponse;
import Utility.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthonticationController {

    @Autowired
    private UserRepository userRepository;

    @PostMapping ("/login")
    public ResponseEntity <?> login(
            @RequestParam String username,
            @RequestParam String password
    ) {
        UserEntity user = userRepository.findByUsername(username);
        if (user != null && new BCryptPasswordEncoder ().matches(password, user.getPassword())) {
            String token = JwtUtil.generateToken(user.getEmail());
            return ResponseEntity.ok(new AuthResponse (token));
        }
        return ResponseEntity.status( HttpStatus.UNAUTHORIZED).body("Invalid username or password");
    }

    @GetMapping ("/protected")
    public ResponseEntity<String> protectedEndpoint( HttpServletRequest request) {
        String userEmail = (String) request.getAttribute("userEmail");
        if (userEmail == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
        }
        // Proceed with the authenticated user's email
        return ResponseEntity.ok("Hello, " + userEmail);
    }


}

