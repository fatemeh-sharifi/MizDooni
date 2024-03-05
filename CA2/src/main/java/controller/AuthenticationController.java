package controller;
import domain.user.User;
import service.MizDooni;
import domain.exception.*;

import service.MizDooni;

public class AuthenticationController {
    private MizDooni mizDooni = MizDooni.getInstance();
    private static AuthenticationController instance;

    private AuthenticationController() {

    }

    public static AuthenticationController getInstance() {
        if (instance == null)
            instance = new AuthenticationController();
        return instance;
    }
    public String login(String username, String password) throws Exception{
        User resUser = validateUsernamePass(username,password);
        return mizDooni.login(resUser);
    }

    private User validateUsernamePass(String username, String password) throws Exception{
        User res = mizDooni.doesAccountExists(username,password);
        if(res  == null){
            new throwUserPassException();
        }
        return res;
    }
}
