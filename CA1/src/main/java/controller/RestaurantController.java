package controller;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import domain.exception.*;
import domain.table.*;
import domain.reservation.Reservation;
import domain.restaurant.Restaurant;
import domain.user.User;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import domain.address.AddressRestaurant;
import service.MizDooni;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;
import java.util.LinkedHashMap;

public class RestaurantController {
    private MizDooni mizDooni = MizDooni.getInstance();
    private ArrayList<String> availableTimes = new ArrayList<String>() {{
        add("00:00");
        add("01:00");
        add("02:00");
        add("03:00");
        add("04:00");
        add("05:00");
        add("06:00");
        add("07:00");
        add("08:00");
        add("09:00");
        add("10:00");
        add("11:00");
        add("12:00");
        add("13:00");
        add("14:00");
        add("15:00");
        add("16:00");
        add("17:00");
        add("18:00");
        add("19:00");
        add("20:00");
        add("21:00");
        add("22:00");
        add("23:00");
    }};

    private ArrayList<String> availableType = new ArrayList<String>() {{
       add("Iranian");
       add("Asian");
       add("Arabian");
       add("Italian");
       add("Fast Food");
    }};


    public void parseArgAdd(String args) throws Exception {
        JSONParser parser = new JSONParser();
        JSONObject jsonObject = (JSONObject) parser.parse(args);
        String name = (String) jsonObject.get("name");
        validateRestaurantName(name);
        String manager = (String) jsonObject.get("managerUsername");
        validateManager(manager);
        String type = (String) jsonObject.get("type");
        validateType(type);
        String description = (String) jsonObject.get("description");
        String startTime = (String) jsonObject.get("startTime");
        String endTime = (String) jsonObject.get("endTime");
        validateTime(startTime,endTime);
        JSONObject addressObject = (JSONObject) jsonObject.get("address");
        validateAddress(addressObject);
        String country = (String) addressObject.get("country");
        String city = (String) addressObject.get("city");
        String street = (String) addressObject.get("street");
        AddressRestaurant addr = new AddressRestaurant(country, city,street);
        Restaurant restaurant = new Restaurant(name,manager,type,startTime,endTime,description,addr);
        mizDooni.addRestaurant(restaurant);
    }

    public List<Restaurant> parseSearchByNameArgs(String args) throws Exception{
        JSONObject jsonObject = (JSONObject) new JSONParser().parse(args);
        String name = (String) jsonObject.get("name");
        doesRestaurantExists(name);
        return mizDooni.getRestaurantsByName(name);
    }

    public List<Restaurant> parseSearchByTypeArgs(String args) throws Exception{
        JSONObject jsonObject = (JSONObject) new JSONParser().parse(args);
        String type = (String) jsonObject.get("type");
        validateType(type);
        List<Restaurant> restaurants = mizDooni.getRestaurantsByType(type);
        if(restaurants.isEmpty()){
            new throwTypeNotExistsException();
        }
        return restaurants;
    }

    private void validateTime(String startTime,String endTime) throws Exception {
        if (!availableTimes.contains(startTime) || !availableTimes.contains(endTime)) {
            new throwWrongTimeException();
        }
    }

    private void validateRestaurantName(String name) throws Exception {
        if(mizDooni.isRestaurantNameExists(name)){
            new throwRestaurantNameExistsException();
        }
    }

    private void validateManager(String username) throws Exception{
        if(!mizDooni.isUserExists(username)){
            new throwUsernameNotExistsException();
        }
        if(!mizDooni.isManager(username)){
            new throwWrongManagerRoleException();
        }
    }

    private void validateAddress(JSONObject addressObject) throws Exception {
        if (!isValidAddress(addressObject)) {
            new throwRestaurantAddressException();
        }
    }

    private boolean isValidAddress(JSONObject addressObject){
        return addressObject.containsKey("country") && addressObject.containsKey("city") && addressObject.containsKey("street");
    }

    private void validateType(String type) throws Exception {
        if (!availableType.contains(type)) {
            new throwWrongTypeException();
        }
    }

    private void doesRestaurantExists(String name) throws Exception {
        if(!mizDooni.isRestaurantNameAvailable(name)){
            new throwRestaurantNameNotExistsException();
        }
    }

    public void parseAddTable(String args) throws Exception {
        JSONParser parser = new JSONParser();
        JSONObject jsonObject = (JSONObject) parser.parse(args);
        String restaurantName = (String) jsonObject.get("restaurantName");
        int tableNumber = ((Long) jsonObject.get("tableNumber")).intValue();
        int seatsNumber = ((Long) jsonObject.get("seatsNumber")).intValue();
        String managerUsername = (String) jsonObject.get("managerUsername");
        validateTable(restaurantName, tableNumber, seatsNumber, managerUsername);
        Restaurant restaurant = mizDooni.getRestaurantByName(restaurantName);
        Table table = new Table(tableNumber, restaurantName, managerUsername, seatsNumber);
        mizDooni.addTable(restaurant, table);
    }

    private void validateTable(String restaurantName, int tableNumber, int seatsNumber, String managerUsername) throws Exception {
        Restaurant restaurant = mizDooni.getRestaurantByName(restaurantName);
        doesRestaurantExists(restaurant.getName());
        validateTableNum(restaurant.getTables(), tableNumber);
        //validateManager(managerUsername);
        validateSeatsNumber(seatsNumber);
    }
    private boolean isValidTableNum (List<Table> tables, int tableNumber){
        for(Table table: tables){
            if(table.getTableNumber() == tableNumber) {
                    return false;
            }
        }
        return true;
    }

    private void validateTableNum(List<Table> tables, int tableNumber) throws Exception {
        if(!isValidTableNum(tables, tableNumber)){
            new throwTableNumAlreadyExistsException();
        }

    }

    private void validateSeatsNumber(int seatsNumber) throws Exception {
        if (seatsNumber <= 0 || seatsNumber != (int) seatsNumber) {
            new throwInvalidSeatsNumberException();
        }
    }
    public int parseArgReserveTable(String args) throws Exception {
        JSONParser parser = new JSONParser();
        JSONObject jsonObject = (JSONObject) parser.parse(args);
        String username = (String) jsonObject.get("username");
        String restaurantName = (String) jsonObject.get("restaurantName");
        int tableNumber = ((Long) jsonObject.get("tableNumber")).intValue();
        String datetimeString = (String) jsonObject.get("datetime");

        // Validate user existence and role
        if (!mizDooni.isUserExists(username)) {
            new throwUsernameNotExistsException();
        } else {
            User user = mizDooni.getUserByUsername(username);
            if (!user.getRole().equals("manager")) {
                throw new Exception("Only managers are allowed to make reservations.");
            }
        }

        // Validate datetime format
        LocalDateTime datetime;
        try {
            datetime = LocalDateTime.parse(datetimeString, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        } catch (DateTimeParseException e) {
            throw new Exception("Invalid datetime format. Please use yyyy-MM-dd HH:mm format.");
        }

        // Check if the datetime is in the future
        if (datetime.isBefore(LocalDateTime.now())) {
            throw new Exception("Reservation datetime should be in the future.");
        }

        // Check if the restaurant exists
        if (!mizDooni.isRestaurantNameExists(restaurantName)) {
            new throwRestaurantNameNotExistsException();
        }

        // Check if the table exists in the restaurant
        Restaurant restaurant = mizDooni.getRestaurantByName(restaurantName);
        List<Table> tables = restaurant.getTables();
        boolean tableExists = false;
        for (Table table : tables) {
            if (table.getTableNumber() == tableNumber) {
                tableExists = true;
                break;
            }
        }
        if (!tableExists) {
            throw new Exception("Table not found in the restaurant.");
        }

        // Check if the table is available at the specified datetime
        if (isTableReserved(restaurantName, tableNumber, datetime)) {
            throw new Exception("Table is already reserved at the specified datetime.");
        }

        // Add reservation
        int reservationNumber = mizDooni.getReservationNumber();
        Reservation reservation = new Reservation(username, restaurantName, tableNumber, reservationNumber, datetime);
        restaurant.addReservation(reservation);
        User user = mizDooni.getUserByUsername(username);
        user.addReservation(reservation);
        mizDooni.setReservationNumber(reservationNumber + 1);
        return mizDooni.getReservationNumber() - 1;
    }

    private boolean isTableReserved(String restaurantName, int tableNumber, LocalDateTime datetime) {
        Restaurant restaurant = mizDooni.getRestaurantByName(restaurantName);
        if (restaurant == null || restaurant.getReservations() == null) {
            return false; // Restaurant not found or reservations list is null
        }

        List<Reservation> reservations = restaurant.getReservations();
        if (reservations.isEmpty()) {
            return false; // No reservations for this restaurant
        }

        // Check if the table is reserved at the specified datetime
        for (Reservation reservation : reservations) {
            if (reservation.getTableNumber() == tableNumber && reservation.getDatetime().equals(datetime)) {
                return true; // Table is reserved at the specified datetime
            }
        }

        return false; // Table is not reserved at the specified datetime
    }


    public void cancelReservation(String args) throws Exception {
        JSONParser parser = new JSONParser();
        JSONObject jsonObject = (JSONObject) parser.parse(args);
        String username = (String) jsonObject.get("username");
        int reservationNumber = ((Long) jsonObject.get("reservationNumber")).intValue();

        // Find the user
        User user = mizDooni.getUserByUsername(username);
        if (user == null) {
            new throwUsernameNotExistsException();
        }

        // Find the reservation
        Reservation reservationToRemove = null;
        for (Reservation reservation : user.getReservations()) {
            if (reservation.getReservationNumber() == reservationNumber) {
                reservationToRemove = reservation;
                break;
            }
        }

        if (reservationToRemove == null) {
            throw new Exception("Reservation not found for the user.");
        }

        // Check if reservation time is in the past
        LocalDateTime currentDateTime = LocalDateTime.now();
        if (reservationToRemove.getDatetime().isBefore(currentDateTime)) {
            throw new Exception("Cannot cancel reservation for past datetime.");
        }

        // Remove the reservation
        user.getReservations().remove(reservationToRemove);
        // Iterate over all restaurants
        for (Restaurant restaurant : mizDooni.getRestaurants()) {
            // Get the list of reservations for the restaurant
            List<Reservation> reservations = restaurant.getReservations();

            // Find the reservation with the specified username and reservation number
            reservationToRemove = null;
            for (Reservation reservation : reservations) {
                if (reservation.getUsername().equals(username) && reservation.getReservationNumber() == reservationNumber) {
                    reservationToRemove = reservation;
                    break;
                }
            }

            // If the reservation was found, remove it and exit the loop
            if (reservationToRemove != null) {
                reservations.remove(reservationToRemove);
                return;
            }
        }

            // If the reservation was not found in any restaurant, throw an exception
            throw new Exception("Reservation not found.");
    }

    public JSONArray showAvailableTables(String args) throws Exception {
        JSONParser parser = new JSONParser();
        JSONObject jsonObject = (JSONObject) parser.parse(args);
        String restaurantName = (String) jsonObject.get("restaurantName");

        // Retrieve the restaurant
        Restaurant restaurant = mizDooni.getRestaurantByName(restaurantName);

        // Construct the response JSON array
        JSONArray availableTablesArray = new JSONArray();

        // Parse the start and end times of the restaurant
        LocalTime startTime = LocalTime.parse(restaurant.getStartTime());
        LocalTime endTime = LocalTime.parse(restaurant.getEndTime());

        // Get today's date
        LocalDate today = LocalDate.now();

        // Get tomorrow's date
        LocalDate tomorrow = today.plusDays(1);

        // Format today and tomorrow as strings in the format "yyyy-MM-dd"
        String todayString = today.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String tomorrowString = tomorrow.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        // Iterate over the tables of the restaurant
        for (Table table : restaurant.getTables()) {
            ObjectMapper objectMapper = new ObjectMapper();
//            JSONObject tableInfo = new JSONObject();
            ObjectNode tableInfo = objectMapper.createObjectNode();
            tableInfo.put("tableNumber", table.getTableNumber());
            tableInfo.put("seatsNumber", table.getSeatsNumber());

            JSONArray availableTimesArray = new JSONArray();

            // Iterate over available times within the restaurant's working hours
            for (LocalTime time = startTime; !time.isAfter(endTime); time = time.plusHours(1)) {
                // Create LocalDateTime for today and tomorrow using the time
                LocalDateTime todayDateTime = LocalDateTime.of(today, time);
                LocalDateTime tomorrowDateTime = LocalDateTime.of(tomorrow, time);

                // Check if the datetime is within the restaurant's working hours
                if (!isTableReserved(restaurantName, table.getTableNumber(), todayDateTime)
                        && !todayDateTime.toLocalTime().isBefore(startTime)
                        && !todayDateTime.toLocalTime().isAfter(endTime)) {
                    availableTimesArray.add(todayString + " " + time);
                }
                if (!isTableReserved(restaurantName, table.getTableNumber(), tomorrowDateTime)
                        && !tomorrowDateTime.toLocalTime().isBefore(startTime)
                        && !tomorrowDateTime.toLocalTime().isAfter(endTime)) {
                    availableTimesArray.add(tomorrowString + " " + time);
                }
            }

            // Add the array of available times to the tableInfo
            tableInfo.put("availableTimes", String.valueOf(availableTimesArray));

            // Create a JSONObject from the LinkedHashMap and add it to the JSONArray
//            JSONObject tableJson = new JSONObject(tableInfo);
            availableTablesArray.add(tableInfo);
        }

        return availableTablesArray;
    }


}
