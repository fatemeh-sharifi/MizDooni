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

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class MizDooni {
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final String USERS_FILE_PATH = "json/users.json";
    private static final String RESTAURANTS_FILE_PATH = "json/restaurants.json";
    private static java.nio.file.Files Files;

    private List<User> users = new ArrayList<>();
    private List<Restaurant> restaurants = new ArrayList<>();
    private List<Feedback> feedbacks = new ArrayList<>();
    private User loggedInUser = null;

    private int lastRestaurantId;
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
            for (Restaurant restaurant : restaurants) {
                // Increment the last assigned ID and set it for the restaurant
                lastRestaurantId++;
                restaurant.setId(lastRestaurantId);
            }
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


}
