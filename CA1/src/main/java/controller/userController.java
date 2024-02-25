package controller;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import domain.user.User;
import domain.address.AddressUser;
import service.MizDooni;
import java.util.regex.Pattern;


public class userController {
    private MizDooni mizDooni = MizDooni.getInstance();
    public void parseArgAdd(String args) throws Exception{
        try {
            JSONParser parser = new JSONParser();
            JSONObject jsonObject = (JSONObject) parser.parse(args);
            String role = (String) jsonObject.get("role");
            if(isRoleTrue(role)){
                String username = (String) jsonObject.get("username");
                if(isUsernameCorrect(username)){
                    String password = (String) jsonObject.get("password");
                    String email = (String) jsonObject.get("email");
                    if(isEmailPatternCorrect(email)){
                        JSONObject addressObject = (JSONObject) jsonObject.get("address");
                        if(isAddressTrue(addressObject)){
                            String country = (String) addressObject.get("country");
                            String city = (String) addressObject.get("city");
                            AddressUser addr = new AddressUser(country,city);
                            User user = new User(username,email,password,addr);
                            mizDooni.addUser(user);
                        }
                        else {
                            throw new Exception("the address must contain city and country!\n");
                        }
                    }
                    else{
                        throw new Exception("the email must be in order of abc@example.com!\n");
                    }

                }
                else{
                    throw new Exception("the username must not contain special character!\n");
                }
            }
            else{
                throw new Exception("the role must be manager or client!\n");
            }
        } catch (ParseException e) {
            throw new Exception(e.getMessage());
        }
    }

    private boolean isAddressTrue(JSONObject addressObject){
        if(addressObject.containsKey("country") && addressObject.containsKey("city")) {
            return true;
        }
        else {
            return false;
        }
    }

    private boolean isRoleTrue(String role){
        if (role.equals("client")  || role.equals("manager") ) {
            return true;
        }
        else{
            return false;
        }
    }

    private boolean isUsernameCorrect(String username){
        if(Pattern.matches("^[._a-zA-Z0-9]+$", username)){
            return true;
        }
        else{
            return false;
        }
    }

    private boolean isEmailPatternCorrect(String email){
        if(Pattern.matches("^[A-Za-z0-9+_.-]+@[A-Za-z]+.com$",email)){
            return true;
        }
        else {
            return false;
        }
    }
}
