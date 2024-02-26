package service;
import java.util.ArrayList;

import domain.address.AddressRestaurant;
import lombok.Getter;
import lombok.Setter;
import domain.user.User;
import domain.restaurant.Restaurant;
import domain.feedback.Feedback;

@Getter
@Setter
public class MizDooni {
    private ArrayList<User> users = new ArrayList<>();
    private ArrayList<Restaurant> restaurants= new ArrayList<>();
    private ArrayList<Feedback> feedbacks = new ArrayList<>();
    private static MizDooni instance;

    public static MizDooni getInstance() {
        if (instance == null) {
            instance = new MizDooni();
        }
        return instance;
    }

    public void addUser(User user) throws Exception{
        if (doesUserExists(user.getUsername(),user.getEmail())){
            users.add(user);
        }
        else{
            throw new Exception("Username or Email Exists!\n");
        }
    }

    private boolean doesUserExists(String username,String email){
        for (User user : users)
            if (user.getUsername().equals(username) || user.getEmail().equals(email))
                return false;

        return true;
    }

    public void addTable(){
        addSampleRestaurant();
    }
    public Restaurant getRestaurantByName(String restaurantName) {
        for (Restaurant restaurant : restaurants) {
            if (restaurant.getName().equals(restaurantName)) {
                return restaurant;
            }
        }
        return null; // Restaurant not found
    }

    public void addRestaurantTEST(Restaurant restaurant) {
        restaurants.add(restaurant);
    }

    public String addSampleRestaurant() {
        AddressRestaurant address = new AddressRestaurant("Iran", "Tehran", "North Kargar");
        Restaurant restaurant = new Restaurant("restaurant1", "user2", "Iranian", "08:00", "23:00", "Open seven days a week", address);
        addRestaurantTEST(restaurant);
        return("DONE");
    }
}
