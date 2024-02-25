import domain.restaurant.RestaurantManagement;
import domain.user.UserManagement;

public class Main {
    public static void main(String[] args) {
//        // Sample JSON data for adding a user
//        String jsonData = "{\"role\": \"client\", \"username\": \"user1\", \"password\": \"1234\", \"email\": \"user1@gmail.com\", \"address\": {\"country\": \"Iran\", \"city\": \"Tehran\"}}";
//        String result = UserManagement.addUser(jsonData);
//        System.out.println(result);
//
//        // Sample JSON data for adding a user : error client/manager
//        jsonData = "{\"role\": \"user\", \"username\": \"user1\", \"password\": \"1234\", \"email\": \"user1@gmail.com\", \"address\": {\"country\": \"Iran\", \"city\": \"Tehran\"}}";
//        result = UserManagement.addUser(jsonData);
//        System.out.println(result);
//
//        // Sample JSON data for adding a user : duplicated username
//        jsonData = "{\"role\": \"client\", \"username\": \"user1\", \"password\": \"1234\", \"email\": \"user1@gmail.com\", \"address\": {\"country\": \"Iran\", \"city\": \"Tehran\"}}";
//        result = UserManagement.addUser(jsonData);
//        System.out.println(result);
//
//        // Sample JSON data for adding a user : duplicated email
//        jsonData = "{\"role\": \"client\", \"username\": \"user2\", \"password\": \"1234\", \"email\": \"user1@gmail.com\", \"address\": {\"country\": \"Iran\", \"city\": \"Tehran\"}}";
//        result = UserManagement.addUser(jsonData);
//        System.out.println(result);
//
//        // Sample JSON data for adding a user : invalid username
//        jsonData = "{\"role\": \"client\", \"username\": \"user@2\", \"password\": \"1234\", \"email\": \"user2@gmail.com\", \"address\": {\"country\": \"Iran\", \"city\": \"Tehran\"}}";
//        result = UserManagement.addUser(jsonData);
//        System.out.println(result);
//
//        // Sample JSON data for adding a user : invalid email
//        jsonData = "{\"role\": \"client\", \"username\": \"user3\", \"password\": \"1234\", \"email\": \"user2gmail.com\", \"address\": {\"country\": \"Iran\", \"city\": \"Tehran\"}}";
//        result = UserManagement.addUser(jsonData);
//        System.out.println(result);
//
//        // Sample JSON data for adding a user : No city
//        jsonData = "{\"role\": \"client\", \"username\": \"user3\", \"password\": \"1234\", \"email\": \"user2@gmail.com\", \"address\": {\"country\": \"Iran\", \"city\": }}";
//        result = UserManagement.addUser(jsonData);
//        System.out.println(result);

        // Sample JSON data for adding a restaurant
        String jsonData = "{\"name\": \"restaurant1\", \"managerUsername\": \"user2\", \"type\": \"Iranian\", \"startTime\": \"08:00\", \"endTime\": \"23:00\", \"description\": \"Open seven days a week\", \"address\": {\"country\": \"Iran\", \"city\": \"Tehran\", \"street\": \"North Kargar\"}}";

        // Call the addRestaurant method and print the result
        String result = RestaurantManagement.addRestaurant(jsonData);
        System.out.println(result);
    }
    }


}
