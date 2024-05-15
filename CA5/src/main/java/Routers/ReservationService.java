package Routers;

import Entity.Reservation.ReservationEntity;
import Model.Reservation.Reservation;
import Repository.Reservation.ReservationRepository;
import Repository.Restaurant.RestaurantRepository;
import Repository.Table.TableRepository;
import Repository.User.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@RestController
public class ReservationService {
    @Autowired
    ReservationRepository reservationRepository;
    @Autowired
    ClientRepository clientRepository;
    @Autowired
    RestaurantRepository restaurantRepository;
    @Autowired
    TableRepository tableRepository;

    @GetMapping("/reservations")
    public ResponseEntity<List<Reservation>> findUserReservations ( @RequestParam String username ) {
        try {
            List<ReservationEntity> userReservations = reservationRepository.findUserReservations(username);
            System.out.println(userReservations.size());
            List<Reservation> reservations = convertEntityToModel(userReservations);
            return ResponseEntity.ok().body(reservations);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.badRequest().body(null); // Return 400 if there's an error
        }
    }

    public List<Reservation> convertEntityToModel ( List<ReservationEntity> entities ) {
        List<Reservation> models = new ArrayList<Reservation>();
        for (ReservationEntity entity : entities) {
            Reservation model = new Reservation();
            model.setCanceled(entity.getCanceled());
            model.setDate(entity.getDate());
            model.setUsername(entity.getUser().getUsername());
            model.setRestaurantName(entity.getRestaurant().getName());
            model.setReservationNumber((int) entity.getReservationNumber());
            model.setRestaurantId(entity.getRestaurant().getId());
            model.setTableNumber((int) entity.getTable().getId());
            model.setTableSeat(entity.getTableSeat());
            model.setTime(entity.getTime());
            models.add(model);
        }
        return models;
    }

    public boolean isReservationTimePassed ( String username, String restaurantName ) {
        // Get the current date and time
        LocalDate currentDate = LocalDate.now();
        LocalTime currentTime = LocalTime.now();

        // Check if there exists any past reservation for the user at the specified restaurant
        List<ReservationEntity> pastReservations = reservationRepository.findPastReservations(username, restaurantName, currentDate, currentTime);

        return !pastReservations.isEmpty();
    }

    @GetMapping("/isAble")
    public ResponseEntity<String> isAbleToReview (
            @RequestParam String username,
            @RequestParam String restaurantName
    ) {
        try {
            if (!isReservationTimePassed(username, restaurantName)) {
                return ResponseEntity.status(400).body("You need to have a past reservation to post a review");
            }
            return ResponseEntity.ok().body("Able to review.");
        } catch (Exception e) {
            return ResponseEntity.status(400).body("Failed: " + e.getMessage());
        }
    }

    @PostMapping("/cancelReservation")
    public ResponseEntity<String> cancelReservation (
            @RequestParam String username,
            @RequestParam String restaurantName,
            @RequestParam int tableNumber,
            @RequestParam int reservationNumber
    ) {
        try {
            reservationRepository.cancelReservation(username, restaurantName, tableNumber, reservationNumber);
            return ResponseEntity.ok().body("Canceled successfully");
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.badRequest().body("Failed to cancel reservation: " + e.getMessage());
        }
    }
    // In YourController.java
    @PostMapping("/addReservation")
    public ResponseEntity<String> addReservation(
            @RequestParam String username,
            @RequestParam String restaurantName,
            @RequestParam int tableNumber,
            @RequestParam String date,
            @RequestParam String time
    ) {
        try {
            // Parse date and time strings into LocalDate and LocalTime objects
            LocalDate reservationDate = LocalDate.parse(date);
            LocalTime reservationTime = LocalTime.parse(time);

            // Call the service method to add the reservation
            addReservation(username, restaurantName, tableNumber, reservationDate, reservationTime);

            return ResponseEntity.ok().body("Reservation successfully added.");
        } catch (Exception e) {
            return ResponseEntity.status(400).body("Failed to add reservation: " + e.getMessage());
        }
    }
    public boolean addReservation(String username, String restaurantName, int tableNumber, LocalDate reservationDate, LocalTime reservationTime) {
        try {
            // Assuming you have a ReservationEntity class and ReservationRepository interface
            // Create a new ReservationEntity object
            ReservationEntity reservation = new ReservationEntity();
            reservation.setUser(clientRepository.findByUsername(username));
            reservation.setRestaurant(restaurantRepository.findByName(restaurantName));
            reservation.setTable(tableRepository.findByIdAndRestaurantName((long) tableNumber, restaurantName));
            reservation.setDate(reservationDate);
            reservation.setTime(reservationTime);
            reservation.setCanceled(false);
            reservation.setTableSeat(tableRepository.findByIdAndRestaurantName((long) tableNumber, restaurantName).getSeatsNumber());
            reservationRepository.save(reservation);
            return true;
        } catch (Exception e) {
            // Log any exceptions that occur during the reservation creation process
            e.printStackTrace();
            // Return false if an exception occurs during the process
            return false;
        }
    }

}
