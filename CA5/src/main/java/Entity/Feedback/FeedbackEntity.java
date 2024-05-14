package Entity.Feedback;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "feedbacks")
public class FeedbackEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username")
    private String username;

    @Column(name = "restaurant_name")
    private String restaurantName;

    @Column(name = "food_rate")
    private double foodRate;

    @Column(name = "service_rate")
    private double serviceRate;

    @Column(name = "ambiance_rate")
    private double ambianceRate;

    @Column(name = "overall_rate")
    private double overallRate;

    @Column(name = "comment")
    private String comment;

    @Column(name = "date_time")
    private LocalDateTime dateTime;

    public FeedbackEntity() {
    }

    public FeedbackEntity(String username, String restaurantName, double foodRate, double serviceRate, double ambianceRate, double overallRate, String comment, LocalDateTime dateTime) {
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
