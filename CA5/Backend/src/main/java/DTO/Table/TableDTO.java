package DTO.Table;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class TableDTO {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "table_number", nullable = false)
    private int tableNumber;

    @Column(name = "restaurant_name", nullable = false)
    private String restaurantName;

    @Column(name = "manager_username", nullable = false)
    private String managerUsername;

    @Column(name = "seats_number", nullable = false)
    private int seatsNumber;

    public TableDTO(){

    }

    public TableDTO(int tableNumber, String restaurantName, String managerUsername, int seatsNumber) {
        this.tableNumber = tableNumber;
        this.restaurantName = restaurantName;
        this.managerUsername = managerUsername;
        this.seatsNumber = seatsNumber;

    }
}
