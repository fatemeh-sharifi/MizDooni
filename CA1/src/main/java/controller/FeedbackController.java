package controller;

import domain.exception.*;
import domain.feedback.Feedback;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import service.MizDooni;

import java.time.LocalDateTime;

public class FeedbackController {
    private MizDooni mizDooni = MizDooni.getInstance();

    public void parseArgAdd(String args) throws Exception {
        JSONObject jsonObject = (JSONObject) new JSONParser().parse(args);

        String username = (String) jsonObject.get("username");
        validateUsername(username);

        String restaurantName = (String) jsonObject.get("restaurantName");
        validateRestaurantExistence(restaurantName);

        double foodRate = getDouble(jsonObject, "foodRate");
        double serviceRate = getDouble(jsonObject, "serviceRate");
        double ambianceRate = getDouble(jsonObject, "ambianceRate");
        double overallRate = getDouble(jsonObject, "overallRate");
        validateRates(foodRate, serviceRate, ambianceRate, overallRate);

        String comment = (String) jsonObject.get("comment");
        validateCommentNotEmpty(comment);
        Feedback feedback = new Feedback(username, restaurantName, foodRate, serviceRate,
                ambianceRate, overallRate, comment, LocalDateTime.now());

        mizDooni.addFeedback(feedback);
    }

    private void validateCommentNotEmpty(String comment) throws Exception {
        if (comment == null || comment.trim ().isEmpty ()) {
            new throwMissingCommentException ();
        }
    }
    private double getDouble(JSONObject jsonObject, String key) throws Exception {
        Number rate = (Number) jsonObject.get(key);
        if (rate == null) {
            new throwJsonException(key);
        }
        return rate.doubleValue();
    }

    private void validateUsername(String username) throws Exception {
        if (!mizDooni.isUserExists(username)) {
            new throwUsernameNotExistsException();
        }
        if (mizDooni.isManager(username)) {
            new throwWrongClientRoleException();
        }
    }

    private void validateRestaurantExistence(String restaurantName) throws Exception {
        if (!mizDooni.isRestaurantNameExists(restaurantName)) {
            new throwRestaurantNameNotExistsException();
        }
    }

    private boolean isValidRange(double rate) {
        return rate >= 0 && rate <= 5;
    }

    private void validateRates(double foodRate, double serviceRate, double ambianceRate, double overallRate) throws Exception {
        if (!isValidRange(foodRate) || !isValidRange(serviceRate) ||
                !isValidRange(ambianceRate) || !isValidRange(overallRate)) {
            new throwRateOutOfRangeException();
        }
    }
}
