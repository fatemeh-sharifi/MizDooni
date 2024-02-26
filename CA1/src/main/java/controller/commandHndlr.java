package controller;
import domain.reservation.Reservation;
import domain.restaurant.Restaurant;
import org.json.simple.JSONObject;

import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import org.json.simple.JSONArray;

public class commandHndlr {
    private Set<String> availableReq = new HashSet<String>() {{
        add("addUser");
        add("addRestaurant");
        add("addTable");
        add("reserveTable");
        add("cancelReservation");
        add("showReservationHistory");
        add("searchRestaurantsByName");
        add("searchRestaurantsByType");
        add("showAvailableTables");
        add("addReview");
    }};
    private UserController usercntrl = new UserController();
    private RestaurantController restaurantController = new RestaurantController();
    public void start() throws InterruptedException {
        System.out.println("welcome!");
        while(true){
            Scanner myObj = new Scanner(System.in);
            System.out.println("to exit enter q else Enter the command : \n");
            String request = myObj.nextLine();
            String[] req = request.split(" ", 2);
            if(this.availableReq.contains(req[0]) && req.length==2){
                try {
                    String res = handleReq(req[0],req[1]);
                    System.out.println(res);
                    clearScreen();
                }catch (Exception e){
                   System.out.println(generateErrorJson(e.getMessage()));
                }

            }
            else if(req[0] == "q"){
                break;
            }
            else{
                System.out.println("request does not exists! try again!\n");
                clearScreen();
            }
        }
    }

    private void clearScreen() throws InterruptedException {
        Thread.sleep(2000);
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    private String handleReq(String req , String args) throws Exception{
        switch(req) {
            case "addUser":
                usercntrl.parseArgAdd(args);
                return generateSuccessJson("User added successfully.\n");
            case "addRestaurant":
                restaurantController.parseArgAdd(args);
                return generateSuccessJson("Restaurant added successfully.\n");
            case "addTable":
                // Handle addTable command
                break;
            case "reserveTable":
                // Handle reserveTable command
                break;
            case "cancelReservation":
                // Handle cancelReservation command
                break;
            case "showReservationHistory":
                List<Reservation> res = usercntrl.parseHistoryArgs(args);
                return generateHistoryJson(res);
            case "searchRestaurantsByName":
                Restaurant restaurant = restaurantController.parseSearchByNameArgs(args);
                // Handle searchRestaurantsByName command
                break;
            case "searchRestaurantsByType":
                // Handle searchRestaurantsByType command
                break;
            case "showAvailableTables":
                // Handle showAvailableTables command
                break;
            case "addReview":
                // Handle addReview command
                break;
            default:
                throw new Exception("Request does not exist!\n");
        }
        return "";
    }

    private static String generateSuccessJson(String message) {
        JSONObject response = new JSONObject();
        response.put("data", message);
        response.put("success", true);
        return response.toJSONString();
    }

    private static String generateErrorJson(String errorMessage) {
        JSONObject response = new JSONObject();
        response.put("data", errorMessage);
        response.put("success", false);
        return response.toJSONString();
    }

    private static String generateHistoryJson(List<Reservation> reservationHistory) {
        JSONArray reservationArray = new JSONArray();

        // Iterate over the list of reservations and add each reservation as a JSON object to the array
        for (Reservation reservation : reservationHistory) {
            JSONObject reservationObject = new JSONObject();
            reservationObject.put("reservationNumber", reservation.getReservationNumber());
            reservationObject.put("restaurantName", reservation.getRestaurantName());
            reservationObject.put("tableNumber", reservation.getTableNumber());
            reservationObject.put("datetime", reservation.getDatetime());
            reservationArray.add(reservationObject);
        }
        JSONObject json = new JSONObject();
        json.put("reservationHistory", reservationArray);
        JSONObject response = new JSONObject();
        response.put("data", json);
        response.put("success", true);
        return response.toJSONString();
    }

    private static String generateSearchByNameJson(Restaurant restaurant){
        JSONArray restaurantArray = new JSONArray();
        JSONObject restaurantObject = new JSONObject();
        restaurantObject.put("name", restaurant.getName());
        restaurantObject.put("type", restaurant.getType());
        restaurantObject.put("startTime", restaurant.getStartTime());
        restaurantObject.put("endTime", restaurant.getEndTime());
        restaurantObject.put("description", restaurant.getDescription());
        JSONObject addressObject = new JSONObject();
        addressObject.put("country", restaurant.getAddress().getCountry());
        addressObject.put("city", restaurant.getAddress().getCity());
        addressObject.put("street", restaurant.getAddress().getStreet());
        restaurantObject.put("address", addressObject);
        restaurantArray.add(restaurantObject);
        JSONObject json = new JSONObject();
        json.put("restaurants", restaurantArray);
        JSONObject response = new JSONObject();
        response.put("data", json);
        response.put("success", true);
        return response.toJSONString();
    }
}
