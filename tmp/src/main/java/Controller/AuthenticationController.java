package Controller;

import Model.Exception.ExceptionMessages;
import Model.Exception.SuperException;
import Model.User.User;
import Service.MizDooni;

public class AuthenticationController {
    private MizDooni mizDooni = MizDooni.getInstance();
    private static AuthenticationController instance;
    private AuthenticationController() {}

    public static AuthenticationController getInstance() {
        if (instance == null)
            instance = new AuthenticationController();
        return instance;
    }

    public String login(String username, String password) throws SuperException {
        User validatedUser = validateUsernamePassword(username, password);
        return mizDooni.login(validatedUser);
    }

    private User validateUsernamePassword(String username, String password) throws SuperException {
        User user = mizDooni.findUserByUsername(username);
        if (user == null || !user.getPassword().equals(password)) {
            throw new SuperException(ExceptionMessages.INVALID_USERNAME_PASSWORD);
        }
        return user;
    }
}
