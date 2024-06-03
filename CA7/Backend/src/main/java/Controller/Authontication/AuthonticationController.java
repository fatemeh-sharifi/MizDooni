package Controller.Authontication;

import Entity.Address.AddressUserEntity;
import Entity.User.UserEntity;
import Model.Address.AddressUser;
import Model.User.User;
import Repository.User.UserRepository;
import Service.User.UserService;
import Model.Response.LoginResponse;
import Utility.JwtUtil;
import co.elastic.apm.api.ElasticApm;
import co.elastic.apm.api.Scope;
import co.elastic.apm.api.Span;
import co.elastic.apm.api.Transaction;
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

//    @GetMapping("/callback")
//    public ResponseEntity<?> callback(@RequestParam(value = "code") String code) throws IOException, InterruptedException {
//        String redirectUri = "http://localhost:3000/callback";
//        String url = "https://oauth2.googleapis.com/token";
//        HttpClient client = HttpClient.newHttpClient();
//        HttpRequest tokenRequest = HttpRequest.newBuilder()
//                .uri(URI.create(url))
//                .POST(HttpRequest.BodyPublishers.ofString(
//                        "code=" + code +
//                                "&client_id=" + CLIENT_ID +
//                                "&client_secret=" + CLIENT_SECRET +
//                                "&redirect_uri=" + redirectUri +
//                                "&grant_type=authorization_code"))
//                .header("Content-Type", "application/x-www-form-urlencoded")
//                .build();
//
//        HttpResponse<String> response = client.send(tokenRequest, HttpResponse.BodyHandlers.ofString());
//        ObjectMapper mapper = new ObjectMapper();
//        Map<String, Object> resultBody = mapper.readValue(response.body(), Map.class);
//        String accessToken = (String) resultBody.get("access_token");
//
//        HttpRequest userInfoRequest = HttpRequest.newBuilder()
//                .uri(URI.create("https://www.googleapis.com/oauth2/v2/userinfo"))
//                .header("Authorization", "Bearer " + accessToken)
//                .build();
//
//        HttpResponse<String> userInfoResponse = client.send(userInfoRequest, HttpResponse.BodyHandlers.ofString());
//        Map<String, Object> userInfo = mapper.readValue(userInfoResponse.body(), Map.class);
//        System.out.println ( userInfo);
//        String email = (String) userInfo.get("email");
//        System.out.println (email );
//        UserEntity user = userRepository.findByEmail(email);
//        if(user != null){
//            String usernameSubstring = email.substring(0, email.indexOf('@'));
//            userRepository.findByEmail ( email ).setUsername (usernameSubstring);
//            System.out.println ("here is user is not null  - " + email);
//        } else {
//            String usernameSubstring = email.substring(0, email.indexOf('@'));
//            AddressUserEntity addressUser = new AddressUserEntity ( "", "" );
//            UserEntity userEntity = new UserEntity ( usernameSubstring, email, "", "client", addressUser);
//            userRepository.save ( userEntity );
//            System.out.println ("here is user is null - " + email );
//        }
////        if (user != null && new BCryptPasswordEncoder ().matches(password, user.getPassword())) {
//        String jwtToken = JwtUtil.generateToken(user.getEmail());
//
//        LoginResponse loginResponse = new LoginResponse ( jwtToken, user );
//
//
//        return ResponseEntity.ok ( loginResponse );
//    }
//    @PostMapping ("/login")
//    public ResponseEntity <?> login(
//            @RequestParam String username,
//            @RequestParam String password
//    ) {
//        UserEntity user = userRepository.findByUsername(username);
//        if (user != null && new BCryptPasswordEncoder ().matches(password, user.getPassword())) {
//            String token = JwtUtil.generateToken(user.getEmail());
//            LoginResponse loginResponse = new LoginResponse (token, user );
//            return ResponseEntity.ok ( loginResponse );
//        }
//        return ResponseEntity.status( HttpStatus.UNAUTHORIZED).body("Invalid username or password");
//    }
//
//    @GetMapping ("/protected")
//    public ResponseEntity<String> protectedEndpoint( HttpServletRequest request) {
//        String userEmail = (String) request.getAttribute("userEmail");
//        if (userEmail == null) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
//        }
//        // Proceed with the authenticated user's email
//        return ResponseEntity.ok("Hello, " + userEmail);
//    }

    @GetMapping("/callback")
    public ResponseEntity<?> callback(@RequestParam(value = "code") String code) throws IOException, InterruptedException {
        Transaction transaction = ElasticApm.startTransaction();
        try (Scope txScope = transaction.activate()) {
            transaction.setName("HTTP GET /callback");
            transaction.setType(Transaction.TYPE_REQUEST);

            String redirectUri = "http://localhost:3000/callback";
            String url = "https://oauth2.googleapis.com/token";
            HttpClient client = HttpClient.newHttpClient();

            // Track the token request
            Span tokenRequestSpan = transaction.startSpan("http", "external", "tokenRequest");
            try (Scope tokenRequestScope = tokenRequestSpan.activate()) {
                tokenRequestSpan.setName("Token Request");

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
                tokenRequestSpan.end();

                ObjectMapper mapper = new ObjectMapper();
                Map<String, Object> resultBody = mapper.readValue(response.body(), Map.class);
                String accessToken = (String) resultBody.get("access_token");

                // Track the user info request
                Span userInfoRequestSpan = transaction.startSpan("http", "external", "userInfoRequest");
                try (Scope userInfoRequestScope = userInfoRequestSpan.activate()) {
                    userInfoRequestSpan.setName("User Info Request");

                    HttpRequest userInfoRequest = HttpRequest.newBuilder()
                            .uri(URI.create("https://www.googleapis.com/oauth2/v2/userinfo"))
                            .header("Authorization", "Bearer " + accessToken)
                            .build();

                    HttpResponse<String> userInfoResponse = client.send(userInfoRequest, HttpResponse.BodyHandlers.ofString());
                    userInfoRequestSpan.end();

                    Map<String, Object> userInfo = mapper.readValue(userInfoResponse.body(), Map.class);
                    String email = (String) userInfo.get("email");

                    // Track the database call
                    Span dbSpan = transaction.startSpan("db", "query", "findUserByEmail");
                    try (Scope dbScope = dbSpan.activate()) {
                        dbSpan.setName("Find User By Email");

                        UserEntity user = userRepository.findByEmail(email);
                        dbSpan.end();

                        if (user != null) {
                            userRepository.findByEmail(email).setUsername(email.substring(0, email.indexOf('@')));
                        } else {
                            AddressUserEntity addressUser = new AddressUserEntity("", "");
                            UserEntity userEntity = new UserEntity(email.substring(0, email.indexOf('@')), email, "", "client", addressUser);
                            userRepository.save(userEntity);
                        }

                        String jwtToken = JwtUtil.generateToken(email);
                        LoginResponse loginResponse = new LoginResponse(jwtToken, user);
                        return ResponseEntity.ok(loginResponse);
                    } catch (Exception e) {
                        dbSpan.captureException(e);
                        dbSpan.end();
                        throw e;
                    }
                } catch (Exception e) {
                    userInfoRequestSpan.captureException(e);
                    userInfoRequestSpan.end();
                    throw e;
                }
            } catch (Exception e) {
                tokenRequestSpan.captureException(e);
                tokenRequestSpan.end();
                throw e;
            }
        } catch (Exception e) {
            transaction.captureException(e);
            return ResponseEntity.badRequest().body(null);
        } finally {
            transaction.end();
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(
            @RequestParam String username,
            @RequestParam String password) {
        Transaction transaction = ElasticApm.startTransaction();
        try (Scope txScope = transaction.activate()) {
            transaction.setName("HTTP POST /login");
            transaction.setType(Transaction.TYPE_REQUEST);

            // Track the database call
            Span dbSpan = transaction.startSpan("db", "query", "findUserByUsername");
            try (Scope dbScope = dbSpan.activate()) {
                dbSpan.setName("Find User By Username");

                UserEntity user = userRepository.findByUsername(username);
                dbSpan.end();

                if (user != null && new BCryptPasswordEncoder().matches(password, user.getPassword())) {
                    String token = JwtUtil.generateToken(user.getEmail());
                    LoginResponse loginResponse = new LoginResponse(token, user);
                    return ResponseEntity.ok(loginResponse);
                }
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
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

    @GetMapping("/protected")
    public ResponseEntity<String> protectedEndpoint(HttpServletRequest request) {
        Transaction transaction = ElasticApm.startTransaction();
        try (Scope txScope = transaction.activate()) {
            transaction.setName("HTTP GET /protected");
            transaction.setType(Transaction.TYPE_REQUEST);

            String userEmail = (String) request.getAttribute("userEmail");
            if (userEmail == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
            }
            // Proceed with the authenticated user's email
            return ResponseEntity.ok("Hello, " + userEmail);
        } catch (Exception e) {
            transaction.captureException(e);
            return ResponseEntity.badRequest().body(null);
        } finally {
            transaction.end();
        }
    }


}

