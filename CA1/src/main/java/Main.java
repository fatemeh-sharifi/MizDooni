import controller.UserController;
import service.MizDooni;

public class Main {
    public static void main(String[] args) {
        UserController userController = new UserController();
        MizDooni mizDooni = new MizDooni();
        try {
//            // Sample JSON data for adding a user
//            String jsonData = "{\"role\": \"client\", \"username\": \"user1\", \"password\": \"1234\", \"email\": \"user1@gmail.com\", \"address\": {\"country\": \"Iran\", \"city\": \"Tehran\"}}";
//            System.out.println("Result for adding user 1: " + addUser(userController, jsonData));
//
//            // Sample JSON data for adding a user: error client/manager
//            jsonData = "{\"role\": \"user\", \"username\": \"user1\", \"password\": \"1234\", \"email\": \"user1@gmail.com\", \"address\": {\"country\": \"Iran\", \"city\": \"Tehran\"}}";
//            System.out.println("Result for adding user 2: " + addUser(userController, jsonData));
//
//            // Sample JSON data for adding a user: duplicated username
//            jsonData = "{\"role\": \"client\", \"username\": \"user1\", \"password\": \"1234\", \"email\": \"user1@gmail.com\", \"address\": {\"country\": \"Iran\", \"city\": \"Tehran\"}}";
//            System.out.println("Result for adding user 3: " + addUser(userController, jsonData));
//
//            // Sample JSON data for adding a user: duplicated email
//            jsonData = "{\"role\": \"client\", \"username\": \"user2\", \"password\": \"1234\", \"email\": \"user1@gmail.com\", \"address\": {\"country\": \"Iran\", \"city\": \"Tehran\"}}";
//            System.out.println("Result for adding user 4: " + addUser(userController, jsonData));
//
//            // Sample JSON data for adding a user: invalid username
//            jsonData = "{\"role\": \"client\", \"username\": \"user@2\", \"password\": \"1234\", \"email\": \"user2@gmail.com\", \"address\": {\"country\": \"Iran\", \"city\": \"Tehran\"}}";
//            System.out.println("Result for adding user 5: " + addUser(userController, jsonData));
//
//            // Sample JSON data for adding a user: invalid email
//            jsonData = "{\"role\": \"client\", \"username\": \"user3\", \"password\": \"1234\", \"email\": \"user2gmail.com\", \"address\": {\"country\": \"Iran\", \"city\": \"Tehran\"}}";
//            System.out.println("Result for adding user 6: " + addUser(userController, jsonData));
//
//            // Sample JSON data for adding a user: No city
//            jsonData = "{\"role\": \"client\", \"username\": \"user3\", \"password\": \"1234\", \"email\": \"user2@gmail.com\", \"address\": {\"country\": \"Iran\", \"city\": }}";
//            System.out.println("Result for adding user 7: " + addUser(userController, jsonData));
            //Sample JSON data for adding a user: No city
            String jsonData = "{\"role\": \"manager\", \"username\": \"user2\", \"password\": \"1234\", \"email\": \"user2@gmail.com\", \"address\": {\"country\": \"Iran\", \"city\": }}";
            System.out.println("Result for adding manager: " + addUser(userController, jsonData));
            System.out.println("Result for adding sample restaurant: " + mizDooni.addSampleRestaurant());
            System.out.println("" + mizDooni.getRestaurantByName("restaurant1"));
        } catch (Exception e) {
            System.err.println("Error occurred: " + e.getMessage());
        }
    }

    private static String addUser(UserController userController, String jsonData) {
        try {
            userController.parseArgAdd(jsonData);
            return "User added successfully!";
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }
}
