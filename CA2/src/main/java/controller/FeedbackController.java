package controller;

import domain.exception.*;
import domain.feedback.Feedback;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import service.MizDooni;

import java.time.LocalDateTime;

public class FeedbackController {
    private MizDooni mizDooni = MizDooni.getInstance ( );
    public void addFeedback (String username , String restaurantName, double foodRate,
                             double serviceRate, double ambianceRate, double overallRate, String comment) throws Exception {
        Feedback feedback = new Feedback ( username , restaurantName , foodRate , serviceRate ,
                ambianceRate , overallRate , comment , LocalDateTime.now ( ) );
        if (!doesFeedbackExists(username, restaurantName)) {
            mizDooni.addFeedback ( feedback );
        }
        else{
            mizDooni.updateFeedback(feedback);
        }
    }
    
    public boolean doesFeedbackExists(String username, String restaurantName){
        if(mizDooni.getFeedbackByName(username,restaurantName) != null){
            return true;
        }
        else{
            return false;
        }
    }
}
