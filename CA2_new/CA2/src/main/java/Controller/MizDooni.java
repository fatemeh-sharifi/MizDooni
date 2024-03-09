//package Controller;
//
//import Model.Restaurant.Restaurant;
//import Model.feedback.Feedback;
//import Model.user.User;
//import com.fasterxml.jackson.core.type.TypeReference;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import lombok.Getter;
//import lombok.Setter;
//
//import java.io.FileNotFoundException;
//import java.io.IOException;
//import java.io.InputStream;
//import java.util.ArrayList;
//import java.util.List;
//
//@Getter
//@Setter
//public class MizDooni {
//    private static final ObjectMapper objectMapper = new ObjectMapper();
//    private static final String USERS_FILE_PATH = "/users.json";
//    private static final String RESTAURANTS_FILE_PATH = "/restaurants.json";
//
//    private List<User> users = new ArrayList<>();
//    private List<Restaurant> restaurants = new ArrayList<>();
//    private List<Feedback> feedbacks = new ArrayList<>();
//    private User loggedInUser = null;
//
//    private static MizDooni instance;
//
//    private MizDooni() {
//        loadUsersFromJson();
//        loadRestaurantsFromJson();
//    }
//
//    public static synchronized MizDooni getInstance() {
//        if (instance == null) {
//            instance = new MizDooni();
//        }
//        return instance;
//    }
//
//    private <T> List<T> loadFromJsonFile(String filePath, Class<T> clazz) throws IOException {
//        ObjectMapper objectMapper = new ObjectMapper();
//        InputStream inputStream = getClass().getResourceAsStream(filePath);
//        if (inputStream != null) {
//            return objectMapper.readValue(inputStream, new TypeReference<List<T>>() {});
//        } else {
//            throw new FileNotFoundException("File not found: " + filePath);
//        }
//    }
//
//
//    public void loadUsersFromJson() {
//        try {
//            List<User> loadedUsers = loadFromJsonFile(USERS_FILE_PATH, User.class);
//            users.addAll(loadedUsers);
//
//            // Print the loaded users
//            System.out.println("Loaded Users:");
//            for (User user : loadedUsers) {
//                System.out.println(user);
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    public void loadRestaurantsFromJson() {
//        try {
//            List<Restaurant> loadedRestaurants = loadFromJsonFile(RESTAURANTS_FILE_PATH, Restaurant.class);
//            restaurants.addAll(loadedRestaurants);
//
//            // Print the loaded restaurants
//            System.out.println("Loaded Restaurants:");
//            for (Restaurant restaurant : loadedRestaurants) {
//                System.out.println(restaurant);
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//
//    public void addUser(User user) {
//        users.add(user);
//    }
//
//    public void addRestaurant(Restaurant restaurant) {
//        restaurants.add(restaurant);
//    }
//
//    public boolean isUserExists(String username) {
//        return users.stream().anyMatch(user -> user.getUsername().equals(username));
//    }
//
//    public boolean isEmailExists(String email) {
//        return users.stream().anyMatch(user -> user.getEmail().equals(email));
//    }
//
//    public boolean isRestaurantNameExists(String name) {
//        return restaurants.stream().anyMatch(restaurant -> restaurant.getName().equals(name));
//    }
//
//    public boolean isManager(String username) {
//        return users.stream().anyMatch(user -> user.getUsername().equals(username) && user.getRole().equals("manager"));
//    }
//}

package Controller;

import Model.Restaurant.Restaurant;
import Model.feedback.Feedback;
import Model.user.User;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class MizDooni {
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final String USERS_FILE_PATH = "json/users.json"; // Assuming files are in the project root
    private static final String RESTAURANTS_FILE_PATH = "json/restaurants.json";
    private static java.nio.file.Files Files;

    private List<User> users = new ArrayList<>();
    private List<Restaurant> restaurants = new ArrayList<>();
    private List<Feedback> feedbacks = new ArrayList<>();
    private User loggedInUser = null;

    private static MizDooni instance;

    private MizDooni() {
        loadUsersFromJson();
        loadRestaurantsFromJson();
    }

    public static synchronized MizDooni getInstance() {
        if (instance == null) {
            instance = new MizDooni();
        }
        return instance;
    }

    public void loadUsersFromJson() {
        try {
            users = loadFromJsonFile(USERS_FILE_PATH, new TypeReference<List<User>>() {});
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadRestaurantsFromJson() {
        try {
            restaurants = loadFromJsonFile(RESTAURANTS_FILE_PATH, new TypeReference<List<Restaurant>>() {});
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private <T> List<T> loadFromJsonFile(String filePath, TypeReference<List<T>> typeReference) throws IOException {
        try (InputStream inputStream = MizDooni.class.getClassLoader().getResourceAsStream(filePath)) {
            if (inputStream == null) {
                throw new IOException("File not found: " + filePath);
            }
            return objectMapper.readValue(inputStream, typeReference);
        }
    }
    private InputStream getFileFromResourceAsStream(String fileName) {

        // The class loader that loaded the class
        ClassLoader classLoader = getClass().getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream(fileName);

        // the stream holding the file content
        if (inputStream == null) {
            throw new IllegalArgumentException("file not found! " + fileName);
        } else {
            return inputStream;
        }

    }

    // print input stream
    private static void printInputStream(InputStream is) {

        try (InputStreamReader streamReader =
                     new InputStreamReader(is, StandardCharsets.UTF_8);
             BufferedReader reader = new BufferedReader(streamReader)) {

            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

//    private <T> List<T> loadFromJsonFile(String filePath, Class<T> clazz) throws IOException {
//        File file = new File(filePath); // Check for file existence
//        InputStream is = getFileFromResourceAsStream(filePath);
////        printInputStream(is);
//        if (!file.exists()) {
//            throw new FileNotFoundException("File not found: " + filePath);
//        }
//
//        try (InputStream inputStream = Files.newInputStream(file.toPath())) {
//            List<T> items = objectMapper.readValue(inputStream, new TypeReference<List<T>>() {});
//            return items;
//        }
//    }
//
//    public void loadUsersFromJson() {
//        try {
//            List<User> loadedUsers = loadFromJsonFile(USERS_FILE_PATH, User.class);
//            users.addAll(loadedUsers);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

//    private InputStream getFileFromResourceAsStream(String fileName) {
//        ClassLoader classLoader = getClass().getClassLoader();
//        InputStream inputStream = classLoader.getResourceAsStream(fileName);
//        return inputStream;
//    }

    //    private <T> List<T> loadFromJsonFile(String filePath, Class<T> clazz) throws IOException {
//        File file = new File(filePath); // Check for file existence
//        InputStream is = getFileFromResourceAsStream(filePath);
//        printInputStream(is);
//        if (!file.exists()) {
//            throw new FileNotFoundException("File not found: " + filePath);
//        }
//
//        try (java.io.InputStream inputStream = java.nio.file.Files.newInputStream(file.toPath())) {
//            return objectMapper.readValue(inputStream, new TypeReference<List<T>>() {
//            });
//        }
//    }


//    public void loadUsersFromJson() {
//        try {
//            List<User> loadedUsers = loadFromJsonFile(USERS_FILE_PATH, User.class);
//            users.addAll(loadedUsers);
//            // Print the loaded users (Optional)
//        } catch (IOException e) {
//            System.err.println("Error loading users: " + e.getMessage());
//            e.printStackTrace();
//        }
//    }

//    public void loadRestaurantsFromJson() {
//        try {
//            List<Restaurant> loadedRestaurants = loadFromJsonFile(RESTAURANTS_FILE_PATH, Restaurant.class);
//            restaurants.addAll(loadedRestaurants);
//
//            // Print the loaded restaurants (Optional)
//        } catch (IOException e) {
//            System.err.println("Error loading restaurants: " + e.getMessage());
//            e.printStackTrace();
//        }
//    }

    // Other methods for user and restaurant management (unchanged)
}
