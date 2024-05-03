//package Controller;
//
//import Model.Exception.SuperException;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//public class LoginController {
//
//    @PostMapping("/login")
//    public ResponseEntity<?> login(@RequestBody LoginRequest request) throws SuperException {
//        // Perform authentication logic here
//        // Validate username and password
//        AuthenticationController authenticationController = new AuthenticationController();
//        // Assuming authentication is successful, return the username
//        authenticationController.validateUsernamePassword(request.getUsername(), request.getPassword());
//        String username = request.getUsername();
//        return ResponseEntity.ok(username);
//    }
//}
