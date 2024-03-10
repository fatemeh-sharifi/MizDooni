package Controller;

import Model.Restaurant.Restaurant;
import Model.feedback.Feedback;
import Model.User.User;
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
            users = loadFromJsonFile(USERS_FILE_PATH, new TypeReference<>() {});
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadRestaurantsFromJson() {
        try {
            restaurants = loadFromJsonFile(RESTAURANTS_FILE_PATH, new TypeReference<>() {});
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

    public String login(User user){
        this.loggedInUser = user;
        String URL ;
        if(isManager(user.getUsername())){
            URL = "views/manager_home.jsp";
        }
        else{
            URL = "views/client_home.jsp";
        }
        return URL;
    }
    public boolean isManager(String username){
        for(User user : users){
            if(user.getUsername().equals(username)){
                return user.getRole().equals("manager");
            }
        }
        return false;
    }
    public boolean isLoggedIn(){
        if (loggedInUser != null){
            return true;
        }
        else{
            return false;
        }
    }
    public String createManagerRestaurantHtml(String username){
        String html = "";
        for (int i=0;i<restaurants.size();i++){
            if (restaurants.get(i).getManagerUsername().equals(username)){
                html += restaurants.get(i).toHtml(i+1);
                break;
            }
        }

        return html;
    }
}