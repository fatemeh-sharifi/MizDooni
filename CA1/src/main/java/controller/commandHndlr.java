package controller;

import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

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
    public void start() throws InterruptedException {
        System.out.println("welcome!");
        while(true){
            Scanner myObj = new Scanner(System.in);
            System.out.println("to exit enter q else Enter the command : \n");
            String request = myObj.nextLine();
            String[] req = request.split(" ", 2);
            if(this.availableReq.contains(req[0]) && req.length==2){
                handleReq(req[0],req[1]);
            }
            else if(req[0] == "q"){
                break;
            }
            else{
                System.out.println("request does not exists! try again!");
                clearScreen();
            }
        }
    }

    private void clearScreen() throws InterruptedException {
        Thread.sleep(2000);
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    private void handleReq(String req , String args){
        switch(req) {
            case "addUser":
                // Handle addUser command
                break;
            case "addRestaurant":
                // Handle addRestaurant command
                break;
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
                // Handle showReservationHistory command
                break;
            case "searchRestaurantsByName":
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
                System.out.println("Request does not exist!\n");
        }
    }

}
