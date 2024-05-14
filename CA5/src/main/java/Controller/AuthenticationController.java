package Controller;

import Model.Exception.ExceptionMessages;
import Model.Exception.SuperException;
import Model.User.User;
import Service.Mizdooni.MizDooni;

public class AuthenticationController {
    private MizDooni mizDooni = MizDooni.getInstance();
    private static AuthenticationController instance;
    AuthenticationController ( ) {}

    public static AuthenticationController getInstance() {
        if (instance == null)
            instance = new AuthenticationController();
        return instance;
    }

//    public String login(String username, String password) throws SuperException {
//        User validatedUser = validateUsernamePassword(username, password);
//        return mizDooni.login(validatedUser);
//    }
    public User loginRestAPI(String username, String password) throws SuperException {
        User validatedUser = validateUsernamePassword(username, password);
        mizDooni.setLoggedInUser(validatedUser);
        return validatedUser;
    }

    public User validateUsernamePassword(String username, String password) throws SuperException {
        User user = mizDooni.findUserByUsername(username);
        if (user == null || !user.getPassword().equals(password)) {
            throw new SuperException(ExceptionMessages.INVALID_USERNAME_PASSWORD);
        }
        return user;
    }
}

