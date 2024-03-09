package Model.Reservation;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class Reservation {
    private String username;
    private String restaurantName;
    private int tableNumber;
    private LocalDateTime datetime;
    private int reservationNumber;

    // Default Constructor
    public Reservation(){

    }

    public Reservation(String userName, String restaurantName, int tableNumber,int reservationNumber, LocalDateTime datetime) {
        this.username = userName;
        this.restaurantName = restaurantName;
        this.tableNumber = tableNumber;
        this.datetime = datetime;
        this.reservationNumber = reservationNumber;
    }
}
