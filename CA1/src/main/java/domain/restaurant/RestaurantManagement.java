package domain.restaurant;

import domain.address.AddressRestaurant;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.util.HashMap;
import java.util.Map;

public class RestaurantManagement {
    private static final Map<String, Restaurant> restaurants = new HashMap<>();
    private static final Map<String, String> managers = new HashMap<>();

    public static String addRestaurant(String jsonData) {
        try {
            JSONParser parser = new JSONParser();
            JSONObject jsonObject = (JSONObject) parser.parse(jsonData);

            // Extract restaurant information from JSON
            String name = (String) jsonObject.get("name");
            String managerUsername = (String) jsonObject.get("managerUsername");
            String type = (String) jsonObject.get("type");
            String startTime = (String) jsonObject.get("startTime");
            String endTime = (String) jsonObject.get("endTime");
            String description = (String) jsonObject.get("description");
            JSONObject addressObject = (JSONObject) jsonObject.get("address");
            String country = (String) addressObject.get("country");
            String city = (String) addressObject.get("city");
            String street = (String) addressObject.get("street");

            // Validate restaurant information
            if (restaurants.containsKey(name)) {
                return generateErrorJson("Restaurant with the same name already exists.");
            }
            if (!managers.containsKey(managerUsername)) {
                return generateErrorJson("Manager username does not exist.");
            }
            if (!managers.get(managerUsername).equals("manager")) {
                return generateErrorJson("The specified manager does not have the manager role.");
            }
            if (!startTime.matches("^(0[0-9]|1[0-9]|2[0-3]):[0-5][0-9]$") || !endTime.matches("^(0[0-9]|1[0-9]|2[0-3]):[0-5][0-9]$")) {
                return generateErrorJson("Invalid start time or end time format.");
            }
            // Validate address
            if (!country.matches("[\\p{L}\\s]+") || !city.matches("[\\p{L}\\s]+") || !street.matches("[\\p{L}\\s]+")) {
                return generateErrorJson("Invalid address format. Country, city, and street must contain only letters and spaces.");
            }

            // Create and add restaurant to the system
            AddressRestaurant address = new AddressRestaurant(country, city, street);
            Restaurant newRestaurant = new Restaurant(name, managerUsername, type, startTime, endTime, description, address);
            restaurants.put(name, newRestaurant);

            return generateSuccessJson("Restaurant added successfully.");
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
