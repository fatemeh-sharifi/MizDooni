import domain.user.UserManagement;

public class Main {
    public static void main(String[] args) {
        // Sample JSON data for adding a user
        String jsonData = "{\"role\": \"client\", \"username\": \"user1\", \"password\": \"1234\", \"email\": \"user1@gmail.com\", \"address\": {\"country\": \"Iran\", \"city\": \"Tehran\"}}";
        String result = UserManagement.addUser(jsonData);
        System.out.println(result);

        // Sample JSON data for adding a user : error client/manager
        jsonData = "{\"role\": \"user\", \"username\": \"user1\", \"password\": \"1234\", \"email\": \"user1@gmail.com\", \"address\": {\"country\": \"Iran\", \"city\": \"Tehran\"}}";
        result = UserManagement.addUser(jsonData);
        System.out.println(result);

        // Sample JSON data for adding a user : duplicated username
        jsonData = "{\"role\": \"client\", \"username\": \"user1\", \"password\": \"1234\", \"email\": \"user1@gmail.com\", \"address\": {\"country\": \"Iran\", \"city\": \"Tehran\"}}";
        result = UserManagement.addUser(jsonData);
        System.out.println(result);

        // Sample JSON data for adding a user : duplicated email
        jsonData = "{\"role\": \"client\", \"username\": \"user2\", \"password\": \"1234\", \"email\": \"user1@gmail.com\", \"address\": {\"country\": \"Iran\", \"city\": \"Tehran\"}}";
        result = UserManagement.addUser(jsonData);
        System.out.println(result);

        // Sample JSON data for adding a user : invalid username
        jsonData = "{\"role\": \"client\", \"username\": \"user@2\", \"password\": \"1234\", \"email\": \"user2@gmail.com\", \"address\": {\"country\": \"Iran\", \"city\": \"Tehran\"}}";
        result = UserManagement.addUser(jsonData);
        System.out.println(result);

        // Sample JSON data for adding a user : invalid email
        jsonData = "{\"role\": \"client\", \"username\": \"user3\", \"password\": \"1234\", \"email\": \"user2gmail.com\", \"address\": {\"country\": \"Iran\", \"city\": \"Tehran\"}}";
        result = UserManagement.addUser(jsonData);
        System.out.println(result);

        // Sample JSON data for adding a user : No city
        jsonData = "{\"role\": \"client\", \"username\": \"user3\", \"password\": \"1234\", \"email\": \"user2@gmail.com\", \"address\": {\"country\": \"Iran\", \"city\": }}";
        result = UserManagement.addUser(jsonData);
        System.out.println(result);
    }


}
