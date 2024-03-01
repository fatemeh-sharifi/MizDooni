package domain.table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Table {
    private int tableNumber;
    private String restaurantName;
    private String managerUsername;
    private int seatsNumber;
    // THIS IS FOR RESERVATION TIME
    private int[] timeSlots;

    public Table(int tableNumber, String restaurantName, String managerUsername, int seatsNumber) {
        this.tableNumber = tableNumber;
        this.restaurantName = restaurantName;
        this.managerUsername = managerUsername;
        this.seatsNumber = seatsNumber;
        this.timeSlots = new int[24];
    }
}
