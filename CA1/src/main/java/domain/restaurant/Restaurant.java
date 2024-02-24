package domain.restaurant;

import java.util.ArrayList;
import java.util.List;

import domain.address.AddressRestaurant;
import lombok.Getter;
import lombok.Setter;
import domain.table.Table;

@Getter
@Setter
public class Restaurant {
    private String name;
    private String managerUsername;
    private String type;
    private String startTime;
    private String endTime;
    private String description;
    private AddressRestaurant address;
    private List<Table> tables;

    public Restaurant(String name, String managerUsername, String type, String startTime, String endTime, String description, AddressRestaurant address) {
        this.name = name;
        this.managerUsername = managerUsername;
        this.type = type;
        this.startTime = startTime;
        this.endTime = endTime;
        this.description = description;
        this.address = address;
        this.tables = new ArrayList<>();
    }


    // Methods to add and remove tables
    public void addTable(Table table) {
        tables.add(table);
    }

    public void removeTable(Table table) {
        tables.remove(table);
    }

}
