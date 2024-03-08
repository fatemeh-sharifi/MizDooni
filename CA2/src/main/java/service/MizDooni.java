package service;
import java.util.ArrayList;
import java.util.List;

import domain.reservation.Reservation;
import domain.table.Table;
import lombok.Getter;
import lombok.Setter;
import domain.user.User;
import domain.restaurant.Restaurant;
import domain.feedback.Feedback;

@Getter
@Setter
public class MizDooni {

    private int reservationNumber = 1;
    private ArrayList<User> users = new ArrayList<>();
    private ArrayList<Restaurant> restaurants= new ArrayList<>();
    private ArrayList<Feedback> feedbacks = new ArrayList<>();
    private static MizDooni instance;
    private User loggedInUser = null;

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

    public List<Feedback> getFeedbacksByName(String username, String restaurantName){
        List<Feedback> res = new ArrayList<>();
        for(Feedback feedback : feedbacks){
            if (feedback.getUsername().equals(username) && feedback.getRestaurantName().equals(restaurantName)){
                res.add(feedback);
            }
        }
        return res;
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
}
