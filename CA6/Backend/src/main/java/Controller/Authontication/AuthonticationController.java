package Controller.Authontication;

import Entity.User.UserEntity;
import Model.User.User;
import Repository.User.UserRepository;
import Service.User.UserService;
import Model.Response.LoginResponse;
import Utility.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;

@RestController
public class AuthonticationController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;
    public static String CLIENT_ID = "318976450429-mlodo6eolob4l5a2mfnb33l6pr8gl3h0.apps.googleusercontent.com";
    public static String CLIENT_SECRET = "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";
    public String ACCESS_TOKEN = "";

    @GetMapping("/callback")
    public ResponseEntity<Map<String, String>> callback(@RequestParam(value = "code") String code) throws IOException, InterruptedException {
        String redirectUri = "http://localhost:3000/callback";
        String url = "https://oauth2.googleapis.com/token";
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest tokenRequest = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .POST(HttpRequest.BodyPublishers.ofString(
                        "code=" + code +
                                "&client_id=" + CLIENT_ID +
                                "&client_secret=" + CLIENT_SECRET +
                                "&redirect_uri=" + redirectUri +
                                "&grant_type=authorization_code"))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .build();

        HttpResponse<String> response = client.send(tokenRequest, HttpResponse.BodyHandlers.ofString());
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> resultBody = mapper.readValue(response.body(), Map.class);
        String accessToken = (String) resultBody.get("access_token");

        HttpRequest userInfoRequest = HttpRequest.newBuilder()
                .uri(URI.create("https://www.googleapis.com/oauth2/v2/userinfo"))
                .header("Authorization", "Bearer " + accessToken)
                .build();

        HttpResponse<String> userInfoResponse = client.send(userInfoRequest, HttpResponse.BodyHandlers.ofString());
        Map<String, Object> userInfo = mapper.readValue(userInfoResponse.body(), Map.class);
        String email = (String) userInfo.get("email");
        String name = (String) userInfo.get("name");

        // Check if the user exists in the database and create/update as necessary
//        User user = userService.findOrCreateUser(email, name);
//
//        // Generate JWT token for the user
//        String jwtToken = userService.createJwtToken(user);
        UserEntity user = userRepository.findByEmail(email);
//        if (user != null && new BCryptPasswordEncoder ().matches(password, user.getPassword())) {
            String jwtToken = JwtUtil.generateToken(user.getEmail());

        // Prepare the response
        Map<String, String> responseMap = new HashMap <> ();
        responseMap.put("token", jwtToken);
        responseMap.put("username", user.getUsername());


        return new ResponseEntity<>(responseMap, HttpStatus.OK);
    }
    @PostMapping ("/login")
    public ResponseEntity <?> login(
            @RequestParam String username,
            @RequestParam String password
    ) {
        UserEntity user = userRepository.findByUsername(username);
        if (user != null && new BCryptPasswordEncoder ().matches(password, user.getPassword())) {
            String token = JwtUtil.generateToken(user.getEmail());
            LoginResponse loginResponse = new LoginResponse (token, user );
            return ResponseEntity.ok ( loginResponse );
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

