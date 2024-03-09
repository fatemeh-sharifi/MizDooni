package model.restaurant;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import model.address.AddressRestaurant;
import model.reservation.Reservation;
import lombok.Getter;
import lombok.Setter;
import model.table.Table;

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
    private List<Reservation> reservations;

    private double serviceAvg;
    private double foodAvg;
    private double ambianceAvg;
    private double overallAvg;

    // Default constructor
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
        this.serviceAvg = 0;
        this.foodAvg=0;
        this.ambianceAvg = 0;
        this.overallAvg = 0;
    }

    public void addTable(Table table) {
        tables.add(table);
    }
    

    public void addReservation(Reservation reservation) {
        reservations.add(reservation);
    }

    public boolean doesReserveExists(String username){
        for(Reservation reservation : reservations){
            if(reservation.getUsername().equals(username)){
                return true;
            }
        }
        return false;
    }

    public boolean isTimeOk(String username){
        for(Reservation reservation : reservations){
            if(reservation.getUsername().equals(username) && reservation.getDatetime().isBefore(LocalDateTime.now())){
                return true;
            }
        }
        return false;
    }

    public void updateRatingsAvg(double food,double service, double ambiance , double overall){
        this.overallAvg = overall;
        this.foodAvg = food;
        this.serviceAvg = service;
        this.ambianceAvg = ambiance;
    }
}
