package Controller;
import Model.Exception.ExceptionMessages;
import Model.Exception.SuperException;
import Model.Reservation.Reservation;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import Model.User.User;
import Model.Address.AddressUser;
import Service.Mizdooni.MizDooni;
import java.util.regex.Pattern;
import java.util.List;

public class UserController {
    private MizDooni mizDooni = MizDooni.getInstance();
    public void parseArgAdd(String args) throws Exception {
        JSONObject jsonObject = (JSONObject) new JSONParser().parse(args);
        validateRole((String) jsonObject.get("role"));
        String username = (String) jsonObject.get("username");
        validateUsername(username);
        String email = (String) jsonObject.get("email");
        validateEmail(email);
        JSONObject addressObject = (JSONObject) jsonObject.get("address");
        validateAddress(addressObject);
        String country = (String) addressObject.get("country");
        String city = (String) addressObject.get("city");
        AddressUser addr = new AddressUser(country, city);
        User user = new User((String) jsonObject.get("username"), (String) jsonObject.get("email"), (String) jsonObject.get("password"), (String) jsonObject.get("role"), addr);
        mizDooni.addUser(user);
    }

    public List<Reservation> parseHistoryArgs(String args) throws Exception{
        JSONObject jsonObject = (JSONObject) new JSONParser().parse(args);
        String username = (String) jsonObject.get("username");
        doesUsernameExists(username);
        return mizDooni.getUserHistory(username);
    }


    private void validateRole(String role) throws Exception {
        if (!isValidRole(role)) {
            throw new SuperException(ExceptionMessages.ROLE_EXCEPTION_MESSAGE);
        }
    }

    private void validateUsername(String username) throws Exception {
        if (!isValidUsername(username)) {
            throw new SuperException(ExceptionMessages.USERNAME_EXCEPTION_MESSAGE);
        }
        if (mizDooni.isUserExists(username)) {
            throw new SuperException(ExceptionMessages.USERNAME_ALREADY_EXISTS_EXCEPTION_MESSAGE);
        }
    }

    private void validateEmail(String email) throws Exception {
        if (!isValidEmail(email)) {
            throw new SuperException(ExceptionMessages.EMAIL_EXCEPTION_MESSAGE);
        }
        if (mizDooni.isEmailExists(email)) {
            throw new SuperException(ExceptionMessages.EMAIL_ALREADY_EXISTS_EXCEPTION_MESSAGE);
        }
    }
    private void validateAddress(JSONObject addressObject) throws Exception {
        if (!isValidAddress(addressObject)) {
            throw new SuperException(ExceptionMessages.ADDRESS_EXCEPTION_MESSAGE);
        }
    }

    private boolean isValidAddress(JSONObject addressObject){
        return addressObject.containsKey("country") && addressObject.containsKey("city");
    }

    private boolean isValidRole(String role){
        return role.equals("client") || role.equals("manager");
    }

    private boolean isValidUsername(String username){
        return Pattern.matches("^[._a-zA-Z0-9]+$", username);
    }

    private boolean isValidEmail(String email){
        return Pattern.matches("^[A-Za-z0-9+_.-]+@[A-Za-z]+.com$", email);
    }

    private void doesUsernameExists(String username) throws Exception{
        if (!mizDooni.isUserExists(username)) {
            throw new SuperException(ExceptionMessages.USERNAME_NOT_EXISTS_EXCEPTION_MESSAGE);
        }
    }

}
