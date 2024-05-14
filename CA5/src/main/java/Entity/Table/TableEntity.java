package Entity.Table;

import Entity.Restaurant.RestaurantEntity;
import Entity.Reservation.ReservationEntity;
import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "tables")
public class TableEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "table_number", nullable = false)
    private int tableNumber;

    @ManyToOne
    @JoinColumn(name = "restaurant_id", nullable = false)
    private RestaurantEntity restaurant;

    @Column(name = "manager_username", nullable = false)
    private String managerUsername;

    @Column(name = "seats_number", nullable = false)
    private int seatsNumber;

    @OneToMany(mappedBy = "table", cascade = CascadeType.ALL)
    private List<ReservationEntity> reservations;

    public TableEntity() {
        this.reservations = new ArrayList<>();
    }

    public TableEntity(int tableNumber, RestaurantEntity restaurant, String managerUsername, int seatsNumber) {
        this.tableNumber = tableNumber;
        this.restaurant = restaurant;
        this.managerUsername = managerUsername;
        this.seatsNumber = seatsNumber;
        this.reservations = new ArrayList<>();
    }
}
