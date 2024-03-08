package controller;

import domain.exception.*;
import domain.feedback.Feedback;
import service.MizDooni;

import java.time.LocalDateTime;


public class FeedbackController {
    private MizDooni mizDooni = MizDooni.getInstance ( );
    private static FeedbackController instance;

    private FeedbackController() {

    }

    public static FeedbackController getInstance() {
        if (instance == null)
            instance = new FeedbackController();
        return instance;
    }
    public void addFeedback (String username , String restaurantName, double foodRate,
                             double serviceRate, double ambianceRate, double overallRate, String comment) throws Exception {
        boolean existence = doesFeedbackExists(username, restaurantName);
        validatePossibilityFeedback(username,restaurantName);
        Feedback feedback = new Feedback ( username , restaurantName , foodRate , serviceRate ,
                ambianceRate , overallRate , comment , LocalDateTime.now ( ) );
        if (!existence) {
            mizDooni.addFeedback ( feedback );
        }
        else{
            mizDooni.updateFeedback(feedback);
        }
        mizDooni.updateRestaurantRatings(restaurantName,foodRate , serviceRate ,
                ambianceRate , overallRate);
    }

    private boolean doesFeedbackExists(String username, String restaurantName){
        if(mizDooni.getFeedbackByName(username,restaurantName) != null){
            return true;
        }
        else{
            return false;
        }
    }

    private void validatePossibilityFeedback(String username, String restaurantName) throws Exception{
        if(!mizDooni.doesUserHaveReserve(username,restaurantName)){
            new throwNoReservationException();
        }
        if(!mizDooni.isFeedbackTimeCorrect(username,restaurantName)){
            new throwTimeFeedbackException();
        }
    }
}
