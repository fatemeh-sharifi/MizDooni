package domain.user;

import domain.address.AddressUser;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.util.HashMap;
import java.util.Map;

public class UserManagement {
    private static final Map<String, User> users = new HashMap<>();

    public static String addUser(String jsonData) {
        try {
            JSONParser parser = new JSONParser();
            JSONObject jsonObject = (JSONObject) parser.parse(jsonData);

            // Extract user information from JSON
            String role = (String) jsonObject.get("role");
            String username = (String) jsonObject.get("username");
            String password = (String) jsonObject.get("password");
            String email = (String) jsonObject.get("email");
            JSONObject addressObject = (JSONObject) jsonObject.get("address");
            String country = (String) addressObject.get("country");
            String city = (String) addressObject.get("city");

            // Validate user information
            if (!role.equals("client") && !role.equals("manager")) {
                return generateErrorJson("Invalid user role. Role must be 'client' or 'manager'.");
            }
            if (users.containsKey(username)) {
                return generateErrorJson("Username already exists.");
            }
            for (User user : users.values()) {
                if (user.getEmail().equals(email)) {
                    return generateErrorJson("Email already exists.");
                }
            }
            if (!username.matches("[\\w]+")) {
                return generateErrorJson("Invalid username format. Username can only contain alphanumeric characters and underscores.");
            }
            if (!email.matches("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}")) {
                return generateErrorJson("Invalid email format.");
            }
            if (!city.matches("[\\p{L}\\s]+") || !country.matches("[\\p{L}\\s]+")) {
                return generateErrorJson("Invalid address format. Country and city must contain only letters and spaces.");
            }

            // Create and add user to the system
            AddressUser address = new AddressUser(country, city);
            User newUser = new User(username, email, password, address);
            users.put(username, newUser);

            return generateSuccessJson("User added successfully.");
        } catch (Exception e) {
            return generateErrorJson("An error occurred while processing the request.");
        }
    }

    private static String generateSuccessJson(String message) {
        JSONObject response = new JSONObject();
        response.put("success", true);
        response.put("data", message);
        return response.toJSONString();
    }

    private static String generateErrorJson(String errorMessage) {
        JSONObject response = new JSONObject();
        response.put("success", false);
        response.put("data", errorMessage);
        return response.toJSONString();
    }
}
