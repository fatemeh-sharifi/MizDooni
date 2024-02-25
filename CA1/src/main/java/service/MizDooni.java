package service;
import java.util.ArrayList;
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


}
