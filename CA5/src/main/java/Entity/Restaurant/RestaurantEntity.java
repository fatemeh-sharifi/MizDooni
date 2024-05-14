package Entity.Restaurant;

import Entity.Address.AddressRestaurantEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "restaurants")
public class RestaurantEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String name;

    @Column(name = "manager_username", nullable = false)
    private String managerUsername;

    @Column(nullable = false)
    private String type;

    @Column(name = "start_time", nullable = false)
    private String startTime;

    @Column(name = "end_time", nullable = false)
    private String endTime;

    @Lob
    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id", referencedColumnName = "id")
    private AddressRestaurantEntity address;

//    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL)
//    private List<TableEntity> tables;
//
//    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL)
//    private List<ReservationEntity> reservations;
//
//    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL)
//    private List<FeedbackEntity> feedbacks;
//
//    @Column(name = "service_avg")
//    private double serviceAvg;
//
//    @Column(name = "food_avg")
//    private double foodAvg;
//
//    @Column(name = "ambiance_avg")
//    private double ambianceAvg;
//
//    @Column(name = "overall_avg")
//    private double overallAvg;

    @Column
    private String image;

    public RestaurantEntity() {
//        this.tables = new ArrayList<>();
//        this.reservations = new ArrayList<>();
//        this.feedbacks = new ArrayList<>();
    }

}
