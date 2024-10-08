package domain.feedback;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Feedback {
    private String username;
    private String restaurantName;
    private double foodRate;
    private double serviceRate;
    private double ambianceRate;
    private double overallRate;
    private String comment;
    private LocalDateTime dateTime;

    public Feedback(String username, String restaurantName, double foodRate, double serviceRate, double ambianceRate, double overallRate, String comment, LocalDateTime dateTime) {
        this.username = username;
        this.restaurantName = restaurantName;
        this.foodRate = foodRate;
        this.serviceRate = serviceRate;
        this.ambianceRate = ambianceRate;
        this.overallRate = overallRate;
        this.comment = comment;
        this.dateTime = dateTime;
    }
}
