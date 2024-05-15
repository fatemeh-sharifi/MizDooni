package Entity.Reservation;

import Entity.Restaurant.RestaurantEntity;
import Entity.Table.TableEntity;
import Entity.User.ClientEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@Entity
@Table(name = "reservations")
public class ReservationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reservation_number", unique = true, nullable = false)
    private long reservationNumber;

//    @Column(name = "username", nullable = false)
//    private String username;
    @ManyToOne
    @JoinColumn(name = "username", referencedColumnName = "username", nullable = false)
    private ClientEntity user;

//    @Column(name = "restaurant_name", nullable = false)
//    private String restaurantName;

    @ManyToOne
    @JoinColumn(name = "restaurant_name", referencedColumnName = "name", nullable = false)
    private RestaurantEntity restaurant;


//    @Column(name = "table_number", nullable = false)
//    private int tableNumber;

    @ManyToOne
    @JoinColumn(name = "table_id", referencedColumnName = "id")
    private TableEntity table;

    @Column(name = "table_seat", nullable = false)
    private int tableSeat;

    @Column(name = "date", nullable = false)
    private LocalDate date;

    @Column(name = "time", nullable = false)
    private LocalTime time;

    @Column(name = "canceled", nullable = false)
    private boolean canceled;


    public ReservationEntity() {
    }

    public boolean getCanceled ( ){
        return canceled;
    }
    public ReservationEntity(ClientEntity user, RestaurantEntity restaurant, TableEntity table, LocalDate date, LocalTime time, int reservationNumber) {
        this.user = user;
        this.restaurant = restaurant;
        this.table = table;
        this.date = date;
        this.time = time;
        this.reservationNumber = reservationNumber;
        this.canceled = false;
    }

}
