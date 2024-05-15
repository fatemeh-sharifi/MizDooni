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

        @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
        @JoinColumn(name = "address_id", referencedColumnName = "id")
        private AddressRestaurantEntity address;


        @Column()
        private String image;

        @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
        @JoinColumn(name = "manager_username", referencedColumnName = "username")
        private UserEntity manager;

        @Column(nullable = false)
        private double serviceAvg;

        @Column(nullable = false)
        private double foodAvg;

        @Column(nullable = false)
        private double ambianceAvg;

        @Column(nullable = false)
        private double overallAvg;


        public RestaurantEntity() {

    }
    // Add a method to generate the ID value based on attributes
    public void generateId() {
        String uniqueString = this.name + this.address.getCity() + this.address.getCountry();
        this.id = Math.abs(uniqueString.hashCode());
    }

}
