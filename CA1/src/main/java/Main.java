import domain.user.UserManagement;

public class Main {
    public static void main(String[] args) {
        // Sample JSON data for adding a user
        String jsonData = "{\"role\": \"client\", \"username\": \"user1\", \"password\": \"1234\", \"email\": \"user1@gmail.com\", \"address\": {\"country\": \"Iran\", \"city\": \"Tehran\"}}";

        // Call the addUser method and print the result
        String result = UserManagement.addUser(jsonData);
        System.out.println(result);
    }
}
