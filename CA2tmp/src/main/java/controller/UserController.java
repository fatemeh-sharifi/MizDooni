package controller;
import domain.reservation.Reservation;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import domain.user.User;
import domain.address.AddressUser;
import service.MizDooni;
import java.util.regex.Pattern;
import domain.exception.*;
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
            new throwRoleException();
        }
    }

    private void validateUsername(String username) throws Exception {
        if (!isValidUsername(username)) {
            new throwUsernameException();
        }
        if (mizDooni.isUserExists(username)) {
            new throwUsernameAlreadyExistsException();
        }
    }

    private void validateEmail(String email) throws Exception {
        if (!isValidEmail(email)) {
            new throwEmailException();
        }
        if (mizDooni.isEmailExists(email)) {
            new throwEmailAlreadyExistsException();
        }
    }
    private void validateAddress(JSONObject addressObject) throws Exception {
        if (!isValidAddress(addressObject)) {
            new throwAddressException();
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
            new throwUsernameNotExistsException();
        }
    }

}
