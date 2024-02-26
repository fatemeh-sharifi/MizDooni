package controller;

import domain.feedback.Feedback;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import service.MizDooni;
import domain.exception.*;

import java.time.LocalDateTime;


public class FeedbackController {
    private MizDooni mizDooni = MizDooni.getInstance();

    public void parseArgAdd(String args) throws Exception {
        JSONParser parser = new JSONParser();
        JSONObject jsonObject = (JSONObject) parser.parse(args);
        String username = (String) jsonObject.get("username");
        validateUsername(username);
        String restaurantName = (String) jsonObject.get("restaurantName");
        validateRestaurantExistence(restaurantName);
        Number foodRate = (Number) jsonObject.get("foodRate");
        Number serviceRate = (Number) jsonObject.get("serviceRate");
        Number ambianceRate = (Number) jsonObject.get("ambianceRate");
        Number overallRate = (Number) jsonObject.get("overallRate");
        validateRates(foodRate,serviceRate,ambianceRate,overallRate);
        String comment = (String) jsonObject.get("comment");
        Feedback feedback = new Feedback(username,restaurantName,foodRate.doubleValue(),serviceRate.doubleValue()
                ,ambianceRate.doubleValue(),overallRate.doubleValue(),comment, LocalDateTime.now());
        mizDooni.addFeedback(feedback);
    }

    private void validateUsername(String username) throws Exception{
        if(!mizDooni.isUserExists(username)){
            new throwUsernameNotExistsException();
        }
        if(mizDooni.isManager(username)){
            new throwWrongClientRoleException();
        }
    }

    private void validateRestaurantExistence(String restaurantName) throws Exception{
        if(!mizDooni.isRestaurantNameExists(restaurantName)){
            new throwRestaurantNameNotExistsException();
        }

    }

    private boolean isValidRange(double rate){
        return rate >= 0 && rate <=5;
    }

    private void validateRates(Number foodRate, Number serviceRate, Number ambianceRate, Number overallRate) throws Exception{
        double foodRateValue = foodRate.doubleValue();
        double serviceRateValue = serviceRate.doubleValue();
        double ambianceRateValue = ambianceRate.doubleValue();
        double overallRateValue = overallRate.doubleValue();
        if(!isValidRange(foodRateValue) || !isValidRange(serviceRateValue) ||
                !isValidRange(ambianceRateValue) || !isValidRange(overallRateValue)){
            new throwRateOutOfRangeException();
        }
    }
}
