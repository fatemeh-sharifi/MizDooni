import controller.RestaurantController;
import controller.UserController;
import org.json.simple.JSONObject;
import org.junit.jupiter.api.*;
import service.MizDooni;

import static org.junit.jupiter.api.Assertions.*;

public class ReservationTest {

    private final UserController userController = new UserController();
    private final RestaurantController restaurantController = new RestaurantController();
    private final MizDooni mizDooni = MizDooni.getInstance();
    @BeforeEach
    public void setUp() throws Exception {
        // Mock manager user
        JSONObject managerJson = new JSONObject();
        managerJson.put("username", "manager");
        managerJson.put("email", "manager@example.com");
        managerJson.put("password", "password123");
        managerJson.put("role", "manager");
        JSONObject managerAddressJson = new JSONObject();
        managerAddressJson.put("country", "Country");
        managerAddressJson.put("city", "City");
        managerJson.put("address", managerAddressJson);
        String managerArgs = managerJson.toString();
        userController.parseArgAdd(managerArgs);

        // Mock client user
        JSONObject clientJson = new JSONObject();
        clientJson.put("username", "client");
        clientJson.put("email", "client@example.com");
        clientJson.put("password", "password456");
        clientJson.put("role", "client");
        JSONObject clientAddressJson = new JSONObject();
        clientAddressJson.put("country", "Country");
        clientAddressJson.put("city", "City");
        clientJson.put("address", clientAddressJson);
        String clientArgs = clientJson.toString();
        userController.parseArgAdd(clientArgs);

        // Mock restaurant
        JSONObject restaurantJson = new JSONObject();
        restaurantJson.put("name", "restaurant1");
        restaurantJson.put("managerUsername", "manager");
        restaurantJson.put("type", "Iranian");
        restaurantJson.put("startTime", "10:00");
        restaurantJson.put("endTime", "22:00");
        restaurantJson.put("description", "Restaurant description");
        JSONObject restaurantAddressObj = new JSONObject();
        restaurantAddressObj.put("country", "Country");
        restaurantAddressObj.put("city", "City");
        restaurantAddressObj.put("street", "Street");
        restaurantJson.put("address", restaurantAddressObj);
        String restaurantArgs = restaurantJson.toString();
        restaurantController.parseArgAdd(restaurantArgs);

        // Mock table
        JSONObject tableJson = new JSONObject();
        tableJson.put("restaurantName", "restaurant1");
        tableJson.put("tableNumber", 1);
        tableJson.put("seatsNumber", 4);
        tableJson.put("managerUsername", "manager");
        String tableArgs = tableJson.toString();
        restaurantController.parseAddTable(tableArgs);
    }

    @AfterEach
    public void tearDown() {
        mizDooni.getUsers().clear();
        mizDooni.getRestaurants().clear();
        mizDooni.getFeedbacks().clear();
        mizDooni.setReservationNumber(1);
    }
    @Test
    public void testValidParameters () {
        String args = "{\"username\": \"client\", \"restaurantName\": \"restaurant1\", \"tableNumber\": 1, \"datetime\": \"3000-02-26 12:00\"}";
        Assertions.assertDoesNotThrow(() -> restaurantController.parseArgReserveTable(args));
        Assertions.assertEquals (1, mizDooni.getRestaurantByName ("restaurant1").getReservations ().size ());
    }
    @Test
    public void testUsernameNotFound () {
        String args = "{\"username\": \"nonexistentuser\", \"restaurantName\": \"restaurant1\", \"tableNumber\": 1, \"datetime\": \"3000-02-26 12:00\"}";
        Exception exception = assertThrows (Exception.class, () -> restaurantController.parseArgReserveTable (args));
        Assertions.assertEquals ("The username does not Exists.\n", exception.getMessage ());
    }
    @Test
    public void testManagerRoleRestriction () {
        String args = "{\"username\": \"manager\", \"restaurantName\": \"restaurant1\", \"tableNumber\": 1, \"datetime\": \"3000-02-26 12:00\"}";
        Exception exception = assertThrows (Exception.class, () -> restaurantController.parseArgReserveTable (args));
        Assertions.assertEquals ("Only clients are allowed to make reservations.\n", exception.getMessage ());
    }
    @Test
    public void testInvalidTimeFormat () {
        String args = "{\"username\": \"client\", \"restaurantName\": \"restaurant1\", \"tableNumber\": 1, \"datetime\": \"3000-02-26 22:30\"}";
        Exception exception = assertThrows (Exception.class, () -> restaurantController.parseArgReserveTable (args));
        Assertions.assertEquals ("The time must be in order of 00:00,01:00,... .\n", exception.getMessage ());
    }
    @Test
    public void testNonExistentRestaurantName () {
        String args = "{\"username\": \"client\", \"restaurantName\": \"nonexistentrestaurant\", \"tableNumber\": 1, \"datetime\": \"3000-02-26 12:00\"}";
        Exception exception = assertThrows (Exception.class, () -> restaurantController.parseArgReserveTable (args));
        Assertions.assertEquals ("The restaurant does not exists.\n", exception.getMessage ());
    }
    @Test
    public void testNonExistentTableNumber () {
        String args = "{\"username\": \"client\", \"restaurantName\": \"restaurant1\", \"tableNumber\": 10, \"datetime\": \"3000-02-26 12:00\"}";
        Exception exception = assertThrows (Exception.class, () -> restaurantController.parseArgReserveTable (args));
        Assertions.assertEquals ("Table not found in the restaurant.\n", exception.getMessage ());
    }
    @Test
    public void testReservedTimeSlot () throws Exception {
        String args1 = "{\"username\": \"client\", \"restaurantName\": \"restaurant1\", \"tableNumber\": 1, \"datetime\": \"3000-02-26 12:00\"}";
        String args2 = "{\"username\": \"client\", \"restaurantName\": \"restaurant1\", \"tableNumber\": 1, \"datetime\": \"3000-02-26 12:00\"}";
        restaurantController.parseArgReserveTable (args1);
        Exception exception = assertThrows (Exception.class, () -> restaurantController.parseArgReserveTable (args2));
        Assertions.assertEquals ("Table is already reserved at the specified datetime.\n", exception.getMessage ());
    }
    @Test
    public void testPastDatetime () {
        String args = "{\"username\": \"client\", \"restaurantName\": \"restaurant1\", \"tableNumber\": 1, \"datetime\": \"2022-01-01 12:00\"}";
        Exception exception = assertThrows (Exception.class, () -> restaurantController.parseArgReserveTable (args));
        Assertions.assertEquals ("Reservation datetime should be in the future.\n", exception.getMessage ());
    }
    @Test
    public void testOutOfRestaurantWorkingHours () {
        String args = "{\"username\": \"client\", \"restaurantName\": \"restaurant1\", \"tableNumber\": 1, \"datetime\": \"3000-02-26 02:00\"}";
        Exception exception = assertThrows (Exception.class, () -> restaurantController.parseArgReserveTable (args));
        Assertions.assertEquals ("The selected date and time is outside of the restaurant's working hours.\n", exception.getMessage ());
    }
}
