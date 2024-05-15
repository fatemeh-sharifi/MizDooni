package Entity.Restaurant;

import Entity.Address.AddressRestaurantEntity;
import Entity.User.UserEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

    @Getter
    @Setter
    @Entity
    @Table(name = "restaurants")
    public class RestaurantEntity {

//        @GeneratedValue(strategy = GenerationType.IDENTITY)
//        @Id
        @Column()
        private int id;

        @Id
        @Column(nullable = false, unique = true)
        private String name;

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

        @Column()
        private String image;

        @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
        @JoinColumn(name = "manager_username", referencedColumnName = "username")
        private UserEntity manager;

//        @Column(name = "manager_username", nullable = false)
//        private String managerUsername;


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

    public RestaurantEntity() {

//        this.tables = new ArrayList<>();
//        this.reservations = new ArrayList<>();
//        this.feedbacks = new ArrayList<>();
    }
    // Add a method to generate the ID value based on attributes
    public void generateId() {
        String uniqueString = this.name + this.address.getCity() + this.address.getCountry();
        this.id = Math.abs(uniqueString.hashCode());
    }

}
