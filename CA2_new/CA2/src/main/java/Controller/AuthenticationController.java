package Controller;

import Model.Exception.ExceptionMessages;
import Model.Exception.SuperException;
import Model.User.User;

public class AuthenticationController {
    private MizDooni mizDooni = MizDooni.getInstance();
    private static AuthenticationController instance;

    private UserController userController = new UserController();
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
        User user = userController.findUserByUsername(username);
        if (user == null || !user.getPassword().equals(password)) {
            throw new SuperException(ExceptionMessages.INVALID_USERNAME_PASSWORD);
        }
        return user;
    }
}
