package controller;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import domain.user.User;
import domain.address.AddressUser;
import service.MizDooni;
import java.util.regex.Pattern;
import domain.exception.*;
public class UserController {
    private MizDooni mizDooni = MizDooni.getInstance();
    public void parseArgAdd(String args) throws Exception {
        JSONObject jsonObject = (JSONObject) new JSONParser().parse(args);
        validateRole((String) jsonObject.get("role"));
        validateUsername((String) jsonObject.get("username"));
        String email = (String) jsonObject.get("email");
        validateEmail(email);
        JSONObject addressObject = (JSONObject) jsonObject.get("address");
        validateAddress(addressObject);

        String country = (String) addressObject.get("country");
        String city = (String) addressObject.get("city");
        AddressUser addr = new AddressUser(country, city);
        User user = new User((String) jsonObject.get("username"), email, (String) jsonObject.get("password"), addr);
        mizDooni.addUser(user);
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
    }

    private void validateEmail(String email) throws Exception {
        if (!isValidEmail(email)) {
            new throwEmailException();
        }
    }

    private void validateAddress(JSONObject addressObject) throws Exception {
        if (!isValidAddress(addressObject)) {
            new throwAddressException();
        }
    }

    private boolean isValidAddress(JSONObject addressObject){
        if(addressObject.containsKey("country") && addressObject.containsKey("city")) {
            return true;
        }
        else {
            return false;
        }
    }

    private boolean isValidRole(String role){
        if (role.equals("client")  || role.equals("manager") ) {
            return true;
        }
        else{
            return false;
        }
    }

    private boolean isValidUsername(String username){
        if(Pattern.matches("^[._a-zA-Z0-9]+$", username)){
            return true;
        }
        else{
            return false;
        }
    }

    private boolean isValidEmail(String email){
        if(Pattern.matches("^[A-Za-z0-9+_.-]+@[A-Za-z]+.com$",email)){
            return true;
        }
        else {
            return false;
        }
    }
}
