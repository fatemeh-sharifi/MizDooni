package Service;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import Model.Exception.ExceptionMessages;
import Model.Exception.SuperException;
import Model.Reservation.Reservation;
import Model.Table.Table;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;
import Model.User.User;
import Model.Restaurant.Restaurant;
import Model.Feedback.Feedback;

@Getter
@Setter
public class MizDooni {

    private int reservationNumber = 1;
    private ArrayList<User> users = new ArrayList<>();
    private ArrayList<Restaurant> restaurants= new ArrayList<>();
    private ArrayList<Feedback> feedbacks = new ArrayList<>();
    private static MizDooni instance;
    private User loggedInUser = null;
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final String USERS_FILE_PATH = "json/users.json";
    private static final String RESTAURANTS_FILE_PATH = "json/restaurants.json";
    private static final String FEEDBACKS_FILE_PATH = "json/feedbacks.json";
    public static MizDooni getInstance() {
        if (instance == null) {
            instance = new MizDooni();
        }
        return instance;
    }


    public void loadUsersFromJson() {
        try {
            users = new ArrayList<>(loadFromJsonFile(USERS_FILE_PATH, new TypeReference<List<User>>() {}));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadRestaurantsFromJson() {
        try {
            restaurants = new ArrayList<>(loadFromJsonFile(RESTAURANTS_FILE_PATH, new TypeReference<List<Restaurant>>() {}));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public <T> List<T> loadFromJsonFile(String filePath, TypeReference<List<T>> typeReference) throws IOException {
        try (InputStream inputStream = MizDooni.class.getClassLoader().getResourceAsStream(filePath)) {
            if (inputStream == null) {
                throw new IOException("File not found: " + filePath);
            }
            return objectMapper.readValue(inputStream, typeReference);
        }
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
    public Feedback getFeedbackByName(String username, String restaurantName){
        for(Feedback feedback: feedbacks){
            if(feedback.getRestaurantName()== restaurantName && feedback.getUsername()==username){
                return feedback;
            }
        }
        return null;
    }

    public void updateFeedback(Feedback feedback){
        this.removeFeedback(feedback.getUsername(),feedback.getRestaurantName());
        this.feedbacks.add(feedback);
    }

    private void removeFeedback(String username, String restaurantName){
        for(int i =0; i < feedbacks.size();i++){
            if(feedbacks.get(i).getRestaurantName()== restaurantName && feedbacks.get(i).getUsername()==username){
                feedbacks.remove(i);
                break;
            }
        }
    }

    public boolean isLoggedIn(){
        if (loggedInUser != null){
            return true;
        }
        else{
            return false;
        }
    }

    public User doesAccountExists(String username, String password){
        for(User user: users){
            if (user.getUsername().equals(username) && user.getPassword().equals(password)){
                return user;
            }
        }
        return null;
    }

    public String login(User user){
        this.loggedInUser = user;
        String URL ;
        if(isManager(user.getUsername())){
            URL = "manager_home.jsp";
        }
        else{
            URL = "client_home.jsp";
        }
        return URL;
    }

    public boolean doesUserHaveReserve(String username, String restaurantName){
        for(Restaurant restaurant: restaurants){
            if(restaurant.getName().equals(restaurantName) && restaurant.doesReserveExists(username)){
                return true;
            }
        }
        return false;
    }

    public boolean isFeedbackTimeCorrect(String username , String restaurantName){
        for(Restaurant restaurant : restaurants){
            if(restaurant.getName().equals(restaurantName) && restaurant.doesReserveExists(username) && restaurant.isTimeOk(username)){
                return true;
            }
        }

        return false;
    }

    public String createFeedbackHTML(String username , String restaurantName){
        String html = "";
        for (Feedback feedback: feedbacks){
            if (feedback.getUsername().equals(username) && feedback.getRestaurantName().equals(restaurantName)){
                html += feedback.toHtml();
            }
        }

        return html;
    }

    public void updateRestaurantRatings(String restaurantName, double foodRate,
                                        double serviceRate, double ambianceRate,double overallRate){
        Restaurant restaurant = this.getRestaurantByName(restaurantName);
        double foodAvg = (foodRate + restaurant.getFoodAvg())/feedbacks.size();
        double serviceAvg = (serviceRate + restaurant.getServiceAvg())/feedbacks.size();
        double ambianceAvg = (ambianceRate + restaurant.getAmbianceAvg())/feedbacks.size();
        double overallAvg = (ambianceRate + restaurant.getOverallAvg())/feedbacks.size();
        restaurant.updateRatingsAvg(foodAvg,serviceAvg,ambianceAvg,overallAvg);
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

    public void logout(){
        this.loggedInUser = null;
    }

    public User findUserByUsername(String username) throws SuperException {
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        throw new SuperException(ExceptionMessages.USER_NOT_FOUND + username);
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

    public List<Restaurant> searchRestaurantsByName(String name) throws SuperException {
        List<Restaurant> restaurantsByName = getRestaurantsByName(name);
        if (restaurantsByName.isEmpty()) {
            throw new SuperException (ExceptionMessages.NO_RESTAURANT_WITH_NAME + name);
        }
        return restaurantsByName;
    }

    public List<Restaurant> searchRestaurantsByType(String type) throws SuperException {
        List<Restaurant> restaurantsByType = getRestaurantsByType(type);
        if (restaurantsByType.isEmpty()) {
            throw new SuperException(ExceptionMessages.NO_RESTAURANT_WITH_TYPE + type);
        }
        return restaurantsByType;
    }

    public List<Restaurant> searchRestaurantsByCity(String city) throws SuperException {
        List<Restaurant> restaurantsByCity = getRestaurantsByCity(city);
        if (restaurantsByCity.isEmpty()) {
            throw new SuperException(ExceptionMessages.NO_RESTAURANT_WITH_CITY + city);
        }
        return restaurantsByCity;
    }

    public List<Restaurant> getRestaurantsByCity(String city){
        List<Restaurant> res = new ArrayList<>();
        for(Restaurant restaurant : restaurants){
            if (restaurant.getAddress().getCity().equals(city)){
                res.add(restaurant);
            }
        }
        return res;
    }
    public List<Restaurant> getRestaurants() {
        return restaurants;
    }

    public List<Restaurant> sortRestaurantsByScore() {
        // Sort the list of restaurants based on overall score
        Collections.sort(restaurants, new Comparator<Restaurant>() {
            @Override
            public int compare(Restaurant restaurant1, Restaurant restaurant2) {
                // Compare based on overall score
                double overallScore1 = (restaurant1.getServiceAvg() + restaurant1.getFoodAvg() + restaurant1.getAmbianceAvg()) / 3.0;
                double overallScore2 = (restaurant2.getServiceAvg() + restaurant2.getFoodAvg() + restaurant2.getAmbianceAvg()) / 3.0;
                return Double.compare(overallScore2, overallScore1); // Descending order
            }
        });

        return restaurants;
    }
    public void loadFeedbacksFromJson() {
    try {
        feedbacks = new ArrayList<>(loadFromJsonFile(FEEDBACKS_FILE_PATH, new TypeReference<List<Feedback>>() {}));
    } catch (IOException e) {
        e.printStackTrace();
    }
    }
    public List<Feedback> getFeedbacksByUsernameAndRestaurantName(String username, String restaurantName) {
        List<Feedback> matchingFeedbacks = new ArrayList<>();
        for (Feedback feedback : feedbacks) {
            if (feedback.getUsername().equals(username) && feedback.getRestaurantName().equals(restaurantName)) {
                matchingFeedbacks.add(feedback);
            }
        }
        return matchingFeedbacks;
    }

}
