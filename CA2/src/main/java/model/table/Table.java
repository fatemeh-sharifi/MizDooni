package model.table;
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
    // Default constructor
    public Table() {
    }
    public Table(int tableNumber, String restaurantName, String managerUsername, int seatsNumber) {
        this.tableNumber = tableNumber;
        this.restaurantName = restaurantName;
        this.managerUsername = managerUsername;
        this.seatsNumber = seatsNumber;
        this.timeSlots = new int[24];
    }

    public String toHtml(){
        return "    <ul>\n" +
                "        <li>table1</li>\n" +
                "        <li>table2</li>\n" +
                "        <li>table3</li>\n" +
                "        <li>table4</li>\n" +
                "        <li>table5</li>\n" +
                "    </ul>\n";
    }
}
