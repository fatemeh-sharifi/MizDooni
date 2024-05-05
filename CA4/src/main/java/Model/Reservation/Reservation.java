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
    private int restaurantId;
    private int tableNumber;
    private int tableSeat;
    private LocalDate date;
    private LocalTime time;

    private int reservationNumber;

    public Reservation() {
    }
    public Reservation(String userName, String restaurantName, int tableNumber,int reservationNumber, LocalDate date, LocalTime time, int restaurantId ,int tableSeat) {
        this.username = userName;
        this.restaurantName = restaurantName;
        this.tableNumber = tableNumber;
        this.date = date;
        this.time = time;
        this.reservationNumber = reservationNumber;
        this.restaurantId = restaurantId;
        this.tableSeat = tableSeat;
    }
}
