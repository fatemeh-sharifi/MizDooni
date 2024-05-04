package Model.Reservation;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Reservation {
    private String username;
    private String restaurantName;
    private int tableNumber;
    private LocalDate date;
    private LocalTime time;

    private int reservationNumber;

    public Reservation() {
    }
    public Reservation(String userName, String restaurantName, int tableNumber,int reservationNumber, LocalDate date, LocalTime time) {
        this.username = userName;
        this.restaurantName = restaurantName;
        this.tableNumber = tableNumber;
        this.date = date;
        this.time = time;
        this.reservationNumber = reservationNumber;
    }
}
