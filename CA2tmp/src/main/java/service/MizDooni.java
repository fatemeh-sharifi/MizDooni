package service;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import domain.reservation.Reservation;
import domain.table.Table;
import lombok.Getter;
import lombok.Setter;
import domain.user.User;
import domain.restaurant.Restaurant;
import domain.feedback.Feedback;
import java.io.File;
import java.io.IOException;
import com.fasterxml.jackson.core.type.TypeReference;

@Getter
@Setter
public class MizDooni {

    private int reservationNumber = 1;
    private ArrayList<User> users = new ArrayList<>();
    private ArrayList<Restaurant> restaurants= new ArrayList<>();
    private ArrayList<Feedback> feedbacks = new ArrayList<>();
    private static MizDooni instance;

    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final String USERS_FILE_PATH = "D:\\New\\Uni_10\\IE\\MizDooni\\CA2tmp\\src\\main\\resources\\users.json";
    private static final String RESTAURANTS_FILE_PATH = "D:\\New\\Uni_10\\IE\\MizDooni\\CA2tmp\\src\\main\\resources\\restaurants.json";

    public void loadUsersFromJson() {
        try {
            File usersFile = new File(USERS_FILE_PATH);
            System.out.println ( "hello" );
            if (usersFile.exists()) {
                System.out.println ( "file path user" );
                users = (ArrayList<User>) objectMapper.readValue(usersFile, new TypeReference<List<User>>() {});
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void loadRestaurantsFromJson() {
        try {
            File restaurantsFile = new File(RESTAURANTS_FILE_PATH);
            if (restaurantsFile.exists()) {
                restaurants = (ArrayList<Restaurant>) objectMapper.readValue(restaurantsFile, new TypeReference<List<Restaurant>>() {});
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static MizDooni getInstance() {
        if (instance == null) {
            instance = new MizDooni();
        }
        return instance;
    }

    public void addUser(User user) throws Exception{
        users.add(user);
    }

    public void addRestaurant(Restaurant restaurant){
        restaurants.add(restaurant);
    }
    public boolean isUserExists(String username) {
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                return true;
            }
        }
        return false; // Username not found
    }

    public void addTable(Restaurant restaurant, Table table){
        restaurant.addTable(table);
    }
    public boolean isEmailExists(String email) {
        for (User user : users) {
            if (user.getEmail().equals(email)) {
                return true;
            }
        }
        return false; // Email not found
    }

    public boolean isRestaurantNameExists(String name){
        for(Restaurant restaurant : restaurants){
            if(restaurant.getName().equals(name)){
                return true;
            }
        }
        return false;
    }

    public boolean isRestaurantNameAvailable(String name){
        for(Restaurant restaurant : restaurants){
            if(restaurant.getName().contains(name)){
                return true;
            }
        }
        return false;
    }

    public boolean isManager(String username){
        for(User user : users){
            if(user.getUsername().equals(username)){
                return user.getRole().equals("manager");
            }
        }
        return false;
    }


    public User getUserByUsername(String username){
        User res = null;
        for(User user : users){
            if(user.getUsername().equals(username))
                res =  user;
        }
        return res;
    }

    public List<Reservation> getUserHistory(String username){
        User user = getUserByUsername(username);
        return user.getReservations();
    }

    public List<Restaurant> getRestaurantsByName(String name){
        List<Restaurant> res = new ArrayList<>();
        for(Restaurant restaurant : restaurants){
            if (restaurant.getName().contains(name)){
                res.add(restaurant);
            }
        }
        return res;
    }

    public List<Restaurant> getRestaurantsByType(String type){
        List<Restaurant> res = new ArrayList<>();
        for(Restaurant restaurant : restaurants){
            if (restaurant.getType().equals(type)){
                res.add(restaurant);
            }
        }
        return res;
    }

    public void addFeedback(Feedback feedback){
        this.feedbacks.add(feedback);
    }

    public Restaurant getRestaurantByName(String restaurantName) {
        for (Restaurant restaurant : restaurants) {
            if (restaurant.getName().equals(restaurantName)) {
                return restaurant;
            }
        }
        return null; // Restaurant not found
    }

}
