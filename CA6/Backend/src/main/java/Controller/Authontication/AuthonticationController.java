package Controller.Authontication;

import Entity.Address.AddressUserEntity;
import Entity.User.UserEntity;
import Model.Address.AddressUser;
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
    public ResponseEntity<?> callback(@RequestParam(value = "code") String code) throws IOException, InterruptedException {
        System.out.println ("HEEEEEEEELLLLLLLLPPPPPPPP" );
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

        String jwtToken ="";
        HttpResponse<String> userInfoResponse = client.send(userInfoRequest, HttpResponse.BodyHandlers.ofString());
        Map<String, Object> userInfo = mapper.readValue(userInfoResponse.body(), Map.class);
        System.out.println ( userInfo);
        String email = (String) userInfo.get("email");
        System.out.println (email );
        UserEntity user = userRepository.findByEmail(email);
        if(user != null){
            String usernameSubstring = email.substring(0, email.indexOf('@'));
            userRepository.findByEmail ( email ).setUsername (usernameSubstring);
            System.out.println ("here is user is not null  - " + email);
            // jwtToken = JwtUtil.generateToken(user.getEmail());
        } else {
            String usernameSubstring = email.substring(0, email.indexOf('@'));
            AddressUserEntity addressUser = new AddressUserEntity ( "", "" );
            UserEntity userEntity = new UserEntity ( usernameSubstring, email, "", "client", addressUser);
            userRepository.save ( userEntity );
            System.out.println ("here is user is null - " + email );
            // jwtToken = JwtUtil.generateToken(userEntity.getEmail());
            user = userEntity;
        }
//        if (user != null && new BCryptPasswordEncoder ().matches(password, user.getPassword())) {
        jwtToken = JwtUtil.generateToken(user.getEmail());

        LoginResponse loginResponse = new LoginResponse ( jwtToken, user );


        return ResponseEntity.ok ( loginResponse );
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

