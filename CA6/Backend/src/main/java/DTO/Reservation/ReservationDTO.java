package DTO.Reservation;

import Entity.Restaurant.RestaurantEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@Entity
public class ReservationDTO {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "restaurant_name", nullable = false)
    private String restaurantName;

    @ManyToOne
    @JoinColumn(name = "restaurant_id", nullable = false)
    private RestaurantEntity restaurant;

    @Column(name = "table_number", nullable = false)
    private int tableNumber;

    @Column(name = "table_seat", nullable = false)
    private int tableSeat;

    @Column(name = "date", nullable = false)
    private LocalDate date;

    @Column(name = "time", nullable = false)
    private LocalTime time;

    @Column(name = "canceled", nullable = false)
    private boolean canceled;

    @Column(name = "reservation_number", nullable = false)
    private int reservationNumber;

    public ReservationDTO() {
    }

    public ReservationDTO(String username, String restaurantName, int tableNumber, int reservationNumber, LocalDate date, LocalTime time, RestaurantEntity restaurant, int tableSeat) {
        this.username = username;
        this.restaurantName = restaurantName;
        this.tableNumber = tableNumber;
        this.reservationNumber = reservationNumber;
        this.date = date;
        this.time = time;
        this.restaurant = restaurant;
        this.tableSeat = tableSeat;
        this.canceled = false;
    }
}
