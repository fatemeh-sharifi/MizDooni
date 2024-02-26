package domain.reservation;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Reservation {
    private String userName;
    private String restaurantName;
    private int tableNumber;
    private LocalDateTime datetime;

    private int reservationNumber;

    public Reservation(String userName, String restaurantName, int tableNumber,int reservationNumber, LocalDateTime datetime) {
        this.userName = userName;
        this.restaurantName = restaurantName;
        this.tableNumber = tableNumber;
        this.datetime = datetime;
        this.reservationNumber = reservationNumber;
    }
}
