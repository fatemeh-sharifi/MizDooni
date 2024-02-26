package controller;
import domain.exception.*;
import domain.restaurant.Restaurant;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import domain.address.AddressRestaurant;
import service.MizDooni;
import java.util.ArrayList;


public class RestaurantController {
    private MizDooni mizDooni = MizDooni.getInstance();
    private ArrayList<String> availableTimes = new ArrayList<String>() {{
        add("00:00");
        add("01:00");
        add("02:00");
        add("03:00");
        add("04:00");
        add("05:00");
        add("06:00");
        add("07:00");
        add("08:00");
        add("09:00");
        add("10:00");
        add("11:00");
        add("12:00");
        add("13:00");
        add("14:00");
        add("15:00");
        add("16:00");
        add("17:00");
        add("18:00");
        add("19:00");
        add("20:00");
        add("21:00");
        add("22:00");
        add("23:00");
    }};

    private ArrayList<String> availableType = new ArrayList<String>() {{
       add("Iranian");
       add("Asian");
       add("Arabian");
       add("Italian");
       add("Fast Food");
    }};


    public void parseArgAdd(String args) throws Exception {
        JSONParser parser = new JSONParser();
        JSONObject jsonObject = (JSONObject) parser.parse(args);
        String name = (String) jsonObject.get("name");
        validateRestaurantName(name);
        String manager = (String) jsonObject.get("managerUsername");
        validateManager(manager);
        String type = (String) jsonObject.get("type");
        validateType(type);
        String description = (String) jsonObject.get("description");
        String startTime = (String) jsonObject.get("startTime");
        String endTime = (String) jsonObject.get("endTime");
        validateTime(startTime,endTime);
        JSONObject addressObject = (JSONObject) jsonObject.get("address");
        validateAddress(addressObject);
        String country = (String) addressObject.get("country");
        String city = (String) addressObject.get("city");
        String street = (String) addressObject.get("street");
        AddressRestaurant addr = new AddressRestaurant(country, city,street);
        Restaurant restaurant = new Restaurant(name,manager,type,startTime,endTime,description,addr);
        mizDooni.addRestaurant(restaurant);
    }

    private void validateTime(String startTime,String endTime) throws Exception {
        if (!availableTimes.contains(startTime) || !availableTimes.contains(endTime)) {
            new throwWrongTimeException();
        }
    }

    private void validateRestaurantName(String name) throws Exception {
        if(mizDooni.isRestaurantNameExists(name)){
            new throwRestaurantNameExistsException();
        }
    }

    private void validateManager(String username) throws Exception{
        if(!mizDooni.isUserExists(username)){
            new throwUsernameNotExistsException();
        }
        if(!mizDooni.isManager(username)){
            new throwWrongManagerRoleException();
        }
    }

    private void validateAddress(JSONObject addressObject) throws Exception {
        if (!isValidAddress(addressObject)) {
            new throwRestaurantAddressException();
        }
    }

    private boolean isValidAddress(JSONObject addressObject){
        return addressObject.containsKey("country") && addressObject.containsKey("city") && addressObject.containsKey("street");
    }

    private void validateType(String type) throws Exception {
        if (!availableType.contains(type)) {
            new throwWrongTypeException();
        }
    }

}
