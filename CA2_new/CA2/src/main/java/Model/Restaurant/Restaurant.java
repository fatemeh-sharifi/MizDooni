package Model.Restaurant;

import Model.Table.Table;
import Model.Address.AddressRestaurant;
import Model.Reservation.Reservation;
import lombok.Getter;
import lombok.Setter;
import java.util.List;
import java.util.ArrayList;

@Getter
@Setter
public class Restaurant {
    private String id;
    private String name;
    private String managerUsername;
    private String type;
    private String startTime;
    private String endTime;
    private String description;
    private AddressRestaurant address;
    private List<Table> tables;
    private List<Reservation> reservations;
    // We should set it later
    private double serviceAvg;
    private double foodAvg;
    private double ambianceAvg;

    //  Default constructor
    public Restaurant() {
        this.tables = new ArrayList<>();
        this.reservations = new ArrayList<>();
    }

    public Restaurant(String name, String managerUsername, String type, String startTime, String endTime, String description, AddressRestaurant address) {
        this.name = name;
        this.managerUsername = managerUsername;
        this.type = type;
        this.startTime = startTime;
        this.endTime = endTime;
        this.description = description;
        this.address = address;
        this.tables = new ArrayList<>();
        this.reservations = new ArrayList<>();

    }
}



//package Model.Restaurant;
//
//import Model.address.AddressRestaurant;
//import Model.reservation.Reservation;
//import Model.table.Table;
//import lombok.Getter;
//import lombok.Setter;
//
//import java.time.LocalDateTime;
//import java.util.ArrayList;
//import java.util.List;
//
//@Getter
//@Setter
//public class Restaurant {
//    private String name;
//    private String managerUsername;
//    private String type;
//    private String startTime;
//    private String endTime;
//    private String description;
//    private AddressRestaurant address;
//    private List<Table> tables;
//    private List<Reservation> reservations;
//
//    private double serviceAvg;
//    private double foodAvg;
//    private double ambianceAvg;
//
//    // Default constructor
//    public Restaurant() {
//        this.tables = new ArrayList<>();
//        this.reservations = new ArrayList<>();
//    }
//
//    public Restaurant(String name, String managerUsername, String type, String startTime, String endTime, String description, AddressRestaurant address) {
//        this.name = name;
//        this.managerUsername = managerUsername;
//        this.type = type;
//        this.startTime = startTime;
//        this.endTime = endTime;
//        this.description = description;
//        this.address = address;
//        this.tables = new ArrayList<>();
//        this.reservations = new ArrayList<>();
//
//    }
//
//    public void addTable(Table table) {
//        tables.add(table);
//    }
//
//
//    public void addReservation(Reservation reservation) {
//        reservations.add(reservation);
//    }
//
//    public boolean doesReserveExists(String username){
//        for(Reservation reservation : reservations){
//            if(reservation.getUsername().equals(username)){
//                return true;
//            }
//        }
//        return false;
//    }
//
//    public boolean isTimeOk(String username){
//        for(Reservation reservation : reservations){
//            if(reservation.getUsername().equals(username) && reservation.getDatetime().isBefore(LocalDateTime.now())){
//                return true;
//            }
//        }
//        return false;
//    }
//}
