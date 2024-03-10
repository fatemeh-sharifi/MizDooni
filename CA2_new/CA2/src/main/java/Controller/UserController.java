package Controller;
import Model.Exception.ExceptionMessages;
import Model.Exception.SuperException;
import Model.User.User;
import java.util.List;
public class UserController {
    private MizDooni mizDooni = MizDooni.getInstance();
    private List <User> users = mizDooni.getUsers();

    public User findUserByUsername(String username) throws SuperException {
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        throw new SuperException(ExceptionMessages.USER_NOT_FOUND + username);
    }
}
