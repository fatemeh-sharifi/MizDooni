package model;

import java.time.LocalDateTime;

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

    // Getters and setters for attributes

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }

    public int getTableNumber() {
        return tableNumber;
    }

    public void setTableNumber(int tableNumber) {
        this.tableNumber = tableNumber;
    }

    public LocalDateTime getDatetime() {
        return datetime;
    }

    public void setDatetime(LocalDateTime datetime) {
        this.datetime = datetime;
    }
}
