package controller.mizdooni;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;
import model.feedback.Feedback;
import model.restaurant.Restaurant;
import model.user.User;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class MizDooni {
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final String USERS_FILE_PATH = "src/main/resources/users.json";
    private static final String RESTAURANTS_FILE_PATH = "src/main/resources/restaurants.json";

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

    private <T> List<T> loadFromJsonFile(String filePath, Class<T> clazz) {
        try {
            File file = new File(filePath);
            if (file.exists()) {
                return objectMapper.readValue(file, new TypeReference<>() {});
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public void loadUsersFromJson() {
        users.addAll(loadFromJsonFile(USERS_FILE_PATH, User.class));
    }

    public void loadRestaurantsFromJson() {
        restaurants.addAll(loadFromJsonFile(RESTAURANTS_FILE_PATH, Restaurant.class));
    }

    public void addUser(User user) {
        users.add(user);
    }

    public void addRestaurant(Restaurant restaurant) {
        restaurants.add(restaurant);
    }

    public boolean isUserExists(String username) {
        return users.stream().anyMatch(user -> user.getUsername().equals(username));
    }

    public boolean isEmailExists(String email) {
        return users.stream().anyMatch(user -> user.getEmail().equals(email));
    }

    public boolean isRestaurantNameExists(String name) {
        return restaurants.stream().anyMatch(restaurant -> restaurant.getName().equals(name));
    }

    public boolean isManager(String username) {
        return users.stream().anyMatch(user -> user.getUsername().equals(username) && user.getRole().equals("manager"));
    }
}
