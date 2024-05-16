package DTO.Feedback;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
public class FeedbackDTO {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

//    @ManyToOne
//    @JoinColumn(name = "customer_username", referencedColumnName = "username")
//    private ClientEntity customer;
//
//    @ManyToOne
//    @JoinColumn(name = "restaurant_name", referencedColumnName = "name")
//    private RestaurantEntity restaurant;

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

    public FeedbackDTO() {
    }

//    public FeedbackEntity( ClientEntity customer, RestaurantEntity restaurant, double foodRate, double serviceRate, double ambianceRate, double overallRate, String comment, LocalDateTime dateTime) {
//        this.customer = customer;
//        this.restaurant = restaurant;
//        this.foodRate = foodRate;
//        this.serviceRate = serviceRate;
//        this.ambianceRate = ambianceRate;
//        this.overallRate = overallRate;
//        this.comment = comment;
//        this.dateTime = dateTime;
//    }
}
