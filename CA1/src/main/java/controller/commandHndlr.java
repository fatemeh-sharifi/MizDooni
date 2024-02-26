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
    private JsonController jsonController = new JsonController();
    private FeedbackController feedbackController = new FeedbackController();
    public void start() throws Exception{
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
                   System.out.println(jsonController.generateErrorJson(e.getMessage()));
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
                return jsonController.generateSuccessJson("User added successfully.");
            case "addRestaurant":
                restaurantController.parseArgAdd(args);
                return jsonController.generateSuccessJson("Restaurant added successfully.");
            case "addTable":
                restaurantController.parseAddTable(args);
                return jsonController.generateSuccessJson("Table added successfully.");
            case "reserveTable":
                int reservationNumber = restaurantController.parseArgReserveTable(args);
                return jsonController.generateSuccessJsonReserveTable(reservationNumber);
            case "cancelReservation":
                restaurantController.cancelReservation(args);
                return jsonController.generateSuccessJson("Reservation cancelled successfully.");
            case "showReservationHistory":
                List<Reservation> res = usercntrl.parseHistoryArgs(args);
                return jsonController.generateHistoryJson(res);
            case "searchRestaurantsByName":
                List<Restaurant> restaurants = restaurantController.parseSearchByNameArgs(args);
                return jsonController.generateSearchByNameJson(restaurants);
            case "searchRestaurantsByType":
                List<Restaurant> restaurantsType = restaurantController.parseSearchByTypeArgs(args);
                return jsonController.generateSearchByNameJson(restaurantsType);
            case "showAvailableTables":
                // Handle showAvailableTables command
                break;
            case "addReview":
                feedbackController.parseArgAdd(args);
                return jsonController.generateSuccessJson("Review added successfully.");
            default:
                throw new Exception("Request does not exist!");
        }
        return "";
    }
}