package Controller;

import Model.Restaurant.Restaurant;
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

    private List<User> users = new ArrayList<>();
    private List<Restaurant> restaurants = new ArrayList<>();
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

    // Authentication Methods
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

    public void logout(){
        this.loggedInUser = null;
    }

    public boolean isLoggedIn(){
        return loggedInUser != null;
    }

    // User Management Methods
    public boolean isManager(String username){
        for(User user : users){
            if(user.getUsername().equals(username)){
                return user.getRole().equals("manager");
            }
        }
        return false;
    }

    // Restaurant Management Methods
    public String generateManagerRestaurantHtml(String username){
        String html = "";
        for (int i=0;i<restaurants.size();i++){
            if (restaurants.get(i).getManagerUsername().equals(username)){
                html += restaurants.get(i).toHtml(i+1);
                break;
            }
        }
        return html;
    }

    private void loadUsersFromJson() {
        try {
            users = loadFromJsonFile(USERS_FILE_PATH, new TypeReference<>() {});
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadRestaurantsFromJson() {
        try {
            restaurants = loadFromJsonFile(RESTAURANTS_FILE_PATH, new TypeReference<>() {});
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
