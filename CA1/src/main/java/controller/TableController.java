package controller;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import domain.restaurant.Restaurant;
import domain.table.Table;
import org.json.simple.parser.ParseException;
import service.MizDooni;

public class TableController {
    private MizDooni mizDooni = MizDooni.getInstance();

    public JSONObject parseAddTableCommand(String args) {
        JSONObject response = new JSONObject();

        try {
            JSONObject jsonObject = (JSONObject) new JSONParser().parse(args);

            int tableNumber = Integer.parseInt(jsonObject.get("tableNumber").toString());
            String restaurantName = (String) jsonObject.get("restaurantName");
            String managerUsername = (String) jsonObject.get("managerUsername");
            int seatsNumber = Integer.parseInt(jsonObject.get("seatsNumber").toString());

//            if (!isValidTableNumber(restaurantName, tableNumber)) {
//                response.put("success", false);
//                response.put("data", "Table with the same number already exists.");
//                return response;
//            }
//
//            if (!isValidManagerUsername(managerUsername)) {
//                response.put("success", false);
//                response.put("data", "Manager username is not valid.");
//                return response;
//            }
//
//            if (!isValidSeatsNumber(seatsNumber)) {
//                response.put("success", false);
//                response.put("data", "Seats number must be a positive integer.");
//                return response;
//            }

            // Adding table to the restaurant
            Restaurant restaurant = mizDooni.getRestaurantByName(restaurantName);
            if (restaurant == null) {
                response.put("success", false);
                response.put("data", "Restaurant not found.");
                return response;
            }

            Table table = new Table(tableNumber, seatsNumber);
            restaurant.addTable(table);

            response.put("success", true);
            response.put("data", "Table added successfully.");
        } catch (ParseException | NumberFormatException e) {
            response.put("success", false);
            response.put("data", "Invalid JSON format.");
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        return response;
    }

//    private boolean isValidTableNumber(String restaurantName, int tableNumber) {
//        Restaurant restaurant = mizDooni.getRestaurantByName(restaurantName);
//        return restaurant == null || restaurant.getTableByNumber(tableNumber) == null;
//    }
//
//    private boolean isValidManagerUsername(String managerUsername) {
//        // Implement validation logic for manager username
//        return true;
//    }
//
//    private boolean isValidSeatsNumber(int seatsNumber) {
//        return seatsNumber > 0;
//    }
}
