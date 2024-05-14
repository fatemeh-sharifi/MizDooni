package Controller;

import Model.Exception.ExceptionMessages;
import Model.Exception.SuperException;
import Model.Feedback.Feedback;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import Service.Mizdooni.MizDooni;

import java.time.LocalDateTime;

public class FeedbackController {
    private MizDooni mizDooni = MizDooni.getInstance ( );
    private static FeedbackController instance;

    public FeedbackController() {

    }

    public static FeedbackController getInstance() {
        if (instance == null)
            instance = new FeedbackController();
        return instance;
    }

    public void parseArgAdd ( String args ) throws Exception {
        JSONObject jsonObject = ( JSONObject ) new JSONParser ( ).parse ( args );

        String username = ( String ) jsonObject.get ( "username" );
        validateUsername ( username );

        String restaurantName = ( String ) jsonObject.get ( "restaurantName" );
        validateRestaurantExistence ( restaurantName );

        double foodRate = getDouble ( jsonObject , "foodRate" );
        double serviceRate = getDouble ( jsonObject , "serviceRate" );
        double ambianceRate = getDouble ( jsonObject , "ambianceRate" );
        double overallRate = getDouble ( jsonObject , "overallRate" );
        validateRates ( foodRate , serviceRate , ambianceRate , overallRate );

        String comment = ( String ) jsonObject.get ( "comment" );
        validateCommentNotEmpty ( comment );
        Feedback feedback = new Feedback ( username , restaurantName , foodRate , serviceRate ,
                ambianceRate , overallRate , comment , LocalDateTime.now ( ) );

        mizDooni.addFeedback ( feedback );
    }

    private void validateCommentNotEmpty ( String comment ) throws Exception {
        if ( comment == null || comment.trim ( ).isEmpty ( ) ) {
            throw new SuperException(ExceptionMessages.MISSING_COMMENT_EXCEPTION_MESSAGE);
        }
    }

    private double getDouble ( JSONObject jsonObject , String key ) throws Exception {
        Number rate = ( Number ) jsonObject.get ( key );
        if ( rate == null ) {
            throw new SuperException(ExceptionMessages.JSON_EXCEPTION_MESSAGE);
        }
        return rate.doubleValue ( );
    }

    private void validateUsername ( String username ) throws Exception {
        if ( ! mizDooni.isUserExists ( username ) ) {
            throw new SuperException(ExceptionMessages.USERNAME_NOT_EXISTS_EXCEPTION_MESSAGE);
        }
        if ( mizDooni.isManager ( username ) ) {
            throw new SuperException(ExceptionMessages.ROLE_EXCEPTION_MESSAGE);
        }
    }

    private void validateRestaurantExistence ( String restaurantName ) throws Exception {
        if ( ! mizDooni.isRestaurantNameExists ( restaurantName ) ) {
            throw new SuperException(ExceptionMessages.RESERVATION_NOT_FOUND_EXCEPTION_MESSAGE);
        }
    }

    private boolean isValidRange ( double rate ) {
        return rate >= 0 && rate <= 5;
    }

    private void validateRates ( double foodRate , double serviceRate , double ambianceRate , double overallRate ) throws Exception {
        if ( ! isValidRange ( foodRate ) || ! isValidRange ( serviceRate ) ||
                ! isValidRange ( ambianceRate ) || ! isValidRange ( overallRate ) ) {
            throw new SuperException(ExceptionMessages.RATE_OUT_OF_RANGE_EXCEPTION_MESSAGE);
        }
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
        mizDooni.updateRestaurantRatings(restaurantName,foodRate,serviceRate,ambianceRate,overallRate);
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
            throw new SuperException(ExceptionMessages.RESERVATION_NOT_FOUND_EXCEPTION_MESSAGE);
        }
        if(!mizDooni.isFeedbackTimeCorrect(username,restaurantName)){
            throw new SuperException((ExceptionMessages.PAST_DATE_TIME_EXCEPTION_MESSAGE));
        }
    }
    //    public List<Feedback> getFeedbacks(String username, String restaurantName){
//        return mizDooni.getFeedbacksByName(username,restaurantName);
//    }
}
