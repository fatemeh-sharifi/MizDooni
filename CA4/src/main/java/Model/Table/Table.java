//package Model.Table;
//
//import lombok.Getter;
//import lombok.Setter;
//
//@Getter
//@Setter
//public class Table {
//    private int tableNumber;
//    private String restaurantName;
//    private String managerUsername;
//    private int seatsNumber;
////    // THIS IS FOR RESERVATION TIME
////    private int[] timeSlots;
//
////    public Table() {
////        this.timeSlots = new int[24];
////    }
//    public Table(){}
//    public Table(int tableNumber, String restaurantName, String managerUsername, int seatsNumber) {
//        this.tableNumber = tableNumber;
//        this.restaurantName = restaurantName;
//        this.managerUsername = managerUsername;
//        this.seatsNumber = seatsNumber;
////        this.timeSlots = new int[24];
//    }
//}
package Model.Table;

import Model.Reservation.Reservation;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.time.LocalTime;
import java.time.LocalDateTime;

@Getter
@Setter
public class Table {
    private int tableNumber;
    private String restaurantName;
    private String managerUsername;
    private int seatsNumber;
    private int openingTime;
    private int closingTime;
    // Map representing reservations for each hour
    private List<Integer> timeSlots;
    // List of reservations for this table
    private List<Reservation> reservations;

    public Table() {
        this.timeSlots = new ArrayList<>() {
        };
        this.reservations = new ArrayList<>();
    }

    public Table(int tableNumber, String restaurantName, String managerUsername, int seatsNumber, int openingTime, int closingTime) {
        this.tableNumber = tableNumber;
        this.restaurantName = restaurantName;
        this.managerUsername = managerUsername;
        this.seatsNumber = seatsNumber;
        this.timeSlots = new ArrayList<>();
        // Initialize all slots between opening and closing time as not reserved
        for (int i = openingTime; i <= closingTime; i++) {
            this.timeSlots.add(i); // Assuming initial state is not reserved
        }
        this.reservations = new ArrayList<>();
    }

    public void makeTimeSlots() {
        for (int i = openingTime; i <= closingTime; i++) {
            this.timeSlots.add(i); // Assuming initial state is not reserved
        }
    }
    public List<Integer> getAvailableTimes(LocalDate date) {
        List<Integer> availableTimes = new ArrayList<>();
        LocalTime currentTime = LocalTime.now();
        LocalDate currentDate = LocalDate.now();
        // Iterate through each time slot
        for (int i = openingTime; i <= closingTime; i++) {
            // Check if the slot is available
            LocalTime timeSlot = LocalTime.of(i, 0);

            // Check if the time slot is after the current time
            if (isTimeSlotAvailable(i, date) && timeSlot.isAfter(currentTime) && date.isAfter(currentDate)) {
                availableTimes.add(i);
            }
        }
        return availableTimes;
    }

    private boolean isTimeSlotAvailable(int timeSlot, LocalDate date) {
        // Check if there's any reservation for the given time slot and date
        for (Reservation reservation : reservations) {
            if (reservation.getDate().equals(date) && reservation.getTime().getHour() == timeSlot) {
                return false; // Slot is reserved
            }
        }
        return true; // Slot is available
    }
    public void addReservation(Reservation res){
        reservations.add(res);
    }

    public Table(int tableNumber, String restaurantName, String managerUsername, int seatsNumber) {
        this.tableNumber = tableNumber;
        this.restaurantName = restaurantName;
        this.managerUsername = managerUsername;
        this.seatsNumber = seatsNumber;
        this.reservations = new ArrayList<>();
    }

    public void cancelReservation(int reservationNumber){
        for(Reservation resrv : reservations){
            if(resrv.getReservationNumber() == reservationNumber){
                resrv.setCalnceled(true);
            }
        }
    }
}
