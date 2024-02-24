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

    public Reservation(String userName, String restaurantName, int tableNumber, LocalDateTime datetime) {
        this.userName = userName;
        this.restaurantName = restaurantName;
        this.tableNumber = tableNumber;
        this.datetime = datetime;
    }

}
