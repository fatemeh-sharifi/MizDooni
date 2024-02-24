package model;

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

    // Getters and setters for attributes

    public int getTableNumber() {
        return tableNumber;
    }

    public void setTableNumber(int tableNumber) {
        this.tableNumber = tableNumber;
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }

    public String getManagerUsername() {
        return managerUsername;
    }

    public void setManagerUsername(String managerUsername) {
        this.managerUsername = managerUsername;
    }

    public int getSeatsNumber() {
        return seatsNumber;
    }

    public void setSeatsNumber(int seatsNumber) {
        this.seatsNumber = seatsNumber;
    }

    // CHECK IT LATER
    public void setTimeSlots(int reserve){
        this.timeSlots[reserve]= 1;
    }

    // CHECK IT LATER
    public int getTimeSlots(int reserve){
        return timeSlots[reserve];
    }
}
