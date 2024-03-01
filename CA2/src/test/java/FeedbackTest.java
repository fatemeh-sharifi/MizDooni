import controller.FeedbackController;
import controller.RestaurantController;
import controller.UserController;
import org.json.simple.JSONObject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.MizDooni;

import static org.junit.jupiter.api.Assertions.*;

public class FeedbackTest {

    private final FeedbackController feedbackController = new FeedbackController();
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
    public void testAddFeedbackWithValidParameters() throws Exception {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("username", "client");
        jsonObject.put("restaurantName", "restaurant1");
        jsonObject.put("foodRate", 4.5);
        jsonObject.put("serviceRate", 4.0);
        jsonObject.put("ambianceRate", 4.5);
        jsonObject.put("overallRate", 4.3);
        jsonObject.put("comment", "Great experience!");

        String args = jsonObject.toJSONString();
        Assertions.assertDoesNotThrow(() -> feedbackController.parseArgAdd(args));
        Assertions.assertEquals (1, mizDooni.getFeedbacks ().size ());

    }

    @Test
    public void testAddFeedbackWithInvalidUsername() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("username", "nonexistentuser");
        jsonObject.put("restaurantName", "restaurant1");
        jsonObject.put("foodRate", 4.5);
        jsonObject.put("serviceRate", 4.0);
        jsonObject.put("ambianceRate", 4.5);
        jsonObject.put("overallRate", 4.3);
        jsonObject.put("comment", "Great experience!");

        String args = jsonObject.toJSONString();
        Exception exception = assertThrows(Exception.class, () -> feedbackController.parseArgAdd(args));
        Assertions.assertEquals("The username does not Exists.\n", exception.getMessage());
    }

    @Test
    public void testAddFeedbackForNonexistentRestaurant() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("username", "client");
        jsonObject.put("restaurantName", "nonexistentrestaurant");
        jsonObject.put("foodRate", 4.5);
        jsonObject.put("serviceRate", 4.0);
        jsonObject.put("ambianceRate", 4.5);
        jsonObject.put("overallRate", 4.3);
        jsonObject.put("comment", "Great experience!");

        String args = jsonObject.toJSONString();
        Exception exception = assertThrows(Exception.class, () -> feedbackController.parseArgAdd(args));
        Assertions.assertEquals("The restaurant does not exists.\n", exception.getMessage());
    }

    @Test
    public void testAddFeedbackWithInvalidRatingRanges() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("username", "client");
        jsonObject.put("restaurantName", "restaurant1");
        jsonObject.put("foodRate", 6.0);
        jsonObject.put("serviceRate", 4.0);
        jsonObject.put("ambianceRate", 4.5);
        jsonObject.put("overallRate", 4.3);
        jsonObject.put("comment", "Great experience!");

        String args = jsonObject.toJSONString();
        Exception exception = assertThrows(Exception.class, () -> feedbackController.parseArgAdd(args));
        Assertions.assertEquals("The rating must be in range of 0 to 5.", exception.getMessage());
    }

    @Test
    public void testAddFeedbackWithMissingRatingFields() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("username", "client");
        jsonObject.put("restaurantName", "restaurant1");
        jsonObject.put("foodRate", 4.5);
        // Missing serviceRate, ambianceRate, and overallRate
        jsonObject.put("comment", "Great experience!");

        String args = jsonObject.toJSONString();
        Exception exception = assertThrows(Exception.class, () -> feedbackController.parseArgAdd(args));
        Assertions.assertEquals("Key 'serviceRate' not found in JSON object.", exception.getMessage());
    }
    @Test
    public void testAddFeedbackWithInvalidUsernameRole() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("username", "manager");
        jsonObject.put("restaurantName", "restaurant1");
        jsonObject.put("foodRate", 4.5);
        jsonObject.put("serviceRate", 4.0);
        jsonObject.put("ambianceRate", 4.5);
        jsonObject.put("overallRate", 4.3);
        jsonObject.put("comment", "Great experience!");

        String args = jsonObject.toJSONString();
        Exception exception = assertThrows(Exception.class, () -> feedbackController.parseArgAdd(args));
        Assertions.assertEquals("The role must be client.", exception.getMessage());
    }

    @Test
    public void testAddFeedbackWithEmptyComment() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("username", "client");
        jsonObject.put("restaurantName", "restaurant1");
        jsonObject.put("foodRate", 4.5);
        jsonObject.put("serviceRate", 4.0);
        jsonObject.put("ambianceRate", 4.5);
        jsonObject.put("overallRate", 4.3);
        jsonObject.put("comment", ""); // Empty comment

        String args = jsonObject.toJSONString();
        Exception exception = assertThrows(Exception.class, () -> feedbackController.parseArgAdd(args));
        Assertions.assertEquals("Feedback comment cannot be empty.\n", exception.getMessage());
    }
}
