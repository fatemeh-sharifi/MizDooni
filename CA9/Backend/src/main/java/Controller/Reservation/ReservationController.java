package Controller.Reservation;

import DTO.AvailableTime.AvailableTimeResponse;
import Entity.Reservation.ReservationEntity;
import Entity.Table.TableEntity;
import Model.Reservation.Reservation;
import Repository.Reservation.ReservationRepository;
import Repository.Restaurant.RestaurantRepository;
import Repository.Table.TableRepository;
import Repository.User.ClientRepository;
import Service.Reservation.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Map;

@RestController
public class ReservationController {
    @Autowired
    ReservationRepository reservationRepository;
    @Autowired
    ClientRepository clientRepository;
    @Autowired
    RestaurantRepository restaurantRepository;
    @Autowired
    TableRepository tableRepository;
    @Autowired
    ReservationService reservationService;

    @GetMapping("/reservations")
    public ResponseEntity<List<Reservation>> findUserReservations ( @RequestParam String username ) {
        try {
            List<ReservationEntity> userReservations = reservationRepository.findUserReservations(username);
            System.out.println(userReservations.size());
            List<Reservation> reservations = reservationService.convertEntityToModel(userReservations);
            return ResponseEntity.ok().body(reservations);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.badRequest().body(null); // Return 400 if there's an error
        }
    }

    @GetMapping("/isAble")
    public ResponseEntity<String> isAbleToReview (
            @RequestParam String username,
            @RequestParam String restaurantName
    ) {
        try {
            if (!reservationService.isReservationTimePassed(username, restaurantName)) {
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
            reservationService.addReservation(username, restaurantName, tableNumber, reservationDate, reservationTime);

            return ResponseEntity.ok().body("Reservation successfully added.");
        } catch (Exception e) {
            return ResponseEntity.status(400).body("Failed to add reservation: " + e.getMessage());
        }
    }
    @GetMapping("/availableTimes")
    public ResponseEntity<AvailableTimeResponse> availableTimes(
            @RequestParam String restaurantName,
            @RequestParam int numberOfPeople,
            @RequestParam String date
    ) {
        try {
            // Validate the date
            LocalDate selectedDate = LocalDate.parse(date);
            LocalDate today = LocalDate.now();
            LocalDate maxAllowedDate = today.plusDays(2); // Maximum allowed date is two days after today
            if (selectedDate.isAfter(maxAllowedDate)) {
                return ResponseEntity.badRequest().body(null); // Date exceeds maximum allowed
            }

            // Retrieve available times from the service
            Map<String, Object> availability = reservationService.getAvailableTimes(restaurantName, numberOfPeople, selectedDate);
            List<Integer> availableTimes = (List<Integer>) availability.get("availableTimes");
            TableEntity availableTable = (TableEntity) availability.get("availableTable");

            if (availableTable == null) {
                return ResponseEntity.notFound().build(); // No available times found
            }

            // Construct and return the response
            AvailableTimeResponse response = new AvailableTimeResponse(availableTimes, availableTable);
            return ResponseEntity.ok(response);

        } catch (DateTimeParseException e) {
            return ResponseEntity.badRequest().body(null); // Invalid date format
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null); // Failed to retrieve available times
        }
    }

}
