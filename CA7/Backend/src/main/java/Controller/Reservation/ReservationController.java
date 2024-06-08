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
import co.elastic.apm.api.ElasticApm;
import co.elastic.apm.api.Scope;
import co.elastic.apm.api.Span;
import co.elastic.apm.api.Transaction;

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

//    @GetMapping("/reservations")
//    public ResponseEntity<List<Reservation>> findUserReservations ( @RequestParam String username ) {
//        try {
//            List<ReservationEntity> userReservations = reservationRepository.findUserReservations(username);
//            System.out.println(userReservations.size());
//            List<Reservation> reservations = reservationService.convertEntityToModel(userReservations);
//            return ResponseEntity.ok().body(reservations);
//        } catch (Exception e) {
//            System.out.println(e.getMessage());
//            return ResponseEntity.badRequest().body(null); // Return 400 if there's an error
//        }
//    }
//
//    @GetMapping("/isAble")
//    public ResponseEntity<String> isAbleToReview (
//            @RequestParam String username,
//            @RequestParam String restaurantName
//    ) {
//        try {
//            if (!reservationService.isReservationTimePassed(username, restaurantName)) {
//                return ResponseEntity.status(400).body("You need to have a past reservation to post a review");
//            }
//            return ResponseEntity.ok().body("Able to review.");
//        } catch (Exception e) {
//            return ResponseEntity.status(400).body("Failed: " + e.getMessage());
//        }
//    }
//
//    @PostMapping("/cancelReservation")
//    public ResponseEntity<String> cancelReservation (
//            @RequestParam String username,
//            @RequestParam String restaurantName,
//            @RequestParam int tableNumber,
//            @RequestParam int reservationNumber
//    ) {
//        try {
//            reservationRepository.cancelReservation(username, restaurantName, tableNumber, reservationNumber);
//            return ResponseEntity.ok().body("Canceled successfully");
//        } catch (Exception e) {
//            System.out.println(e.getMessage());
//            return ResponseEntity.badRequest().body("Failed to cancel reservation: " + e.getMessage());
//        }
//    }
//    @PostMapping("/addReservation")
//    public ResponseEntity<String> addReservation(
//            @RequestParam String username,
//            @RequestParam String restaurantName,
//            @RequestParam int tableNumber,
//            @RequestParam String date,
//            @RequestParam String time
//    ) {
//        try {
//            // Parse date and time strings into LocalDate and LocalTime objects
//            LocalDate reservationDate = LocalDate.parse(date);
//            LocalTime reservationTime = LocalTime.parse(time);
//
//            // Call the service method to add the reservation
//            reservationService.addReservation(username, restaurantName, tableNumber, reservationDate, reservationTime);
//
//            return ResponseEntity.ok().body("Reservation successfully added.");
//        } catch (Exception e) {
//            return ResponseEntity.status(400).body("Failed to add reservation: " + e.getMessage());
//        }
//    }
//    @GetMapping("/availableTimes")
//    public ResponseEntity<AvailableTimeResponse> availableTimes(
//            @RequestParam String restaurantName,
//            @RequestParam int numberOfPeople,
//            @RequestParam String date
//    ) {
//        try {
//            // Validate the date
//            LocalDate selectedDate = LocalDate.parse(date);
//            LocalDate today = LocalDate.now();
//            LocalDate maxAllowedDate = today.plusDays(2); // Maximum allowed date is two days after today
//            if (selectedDate.isAfter(maxAllowedDate)) {
//                return ResponseEntity.badRequest().body(null); // Date exceeds maximum allowed
//            }
//
//            // Retrieve available times from the service
//            Map<String, Object> availability = reservationService.getAvailableTimes(restaurantName, numberOfPeople, selectedDate);
//            List<Integer> availableTimes = (List<Integer>) availability.get("availableTimes");
//            TableEntity availableTable = (TableEntity) availability.get("availableTable");
//
//            if (availableTable == null) {
//                return ResponseEntity.notFound().build(); // No available times found
//            }
//
//            // Construct and return the response
//            AvailableTimeResponse response = new AvailableTimeResponse(availableTimes, availableTable);
//            return ResponseEntity.ok(response);
//
//        } catch (DateTimeParseException e) {
//            return ResponseEntity.badRequest().body(null); // Invalid date format
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null); // Failed to retrieve available times
//        }
//    }
    @GetMapping("/reservations")
    public ResponseEntity<List<Reservation>> findUserReservations(@RequestParam String username) {
        Transaction transaction = ElasticApm.startTransaction();
        try (Scope txScope = transaction.activate()) {
            transaction.setName("HTTP GET /reservations");
            transaction.setType(Transaction.TYPE_REQUEST);

            Span dbSpan = transaction.startSpan("db", "query", "findUserReservations");
            try (Scope dbScope = dbSpan.activate()) {
                dbSpan.setName("Find User Reservations");

                List<ReservationEntity> userReservations = reservationRepository.findUserReservations(username);
                dbSpan.end();

                Span businessLogicSpan = transaction.startSpan("custom", "logic", "convertEntityToModel");
                try (Scope businessLogicScope = businessLogicSpan.activate()) {
                    businessLogicSpan.setName("Convert Entity To Model");

                    List<Reservation> reservations = reservationService.convertEntityToModel(userReservations);
                    businessLogicSpan.end();

                    return ResponseEntity.ok().body(reservations);
                } catch (Exception e) {
                    businessLogicSpan.captureException(e);
                    businessLogicSpan.end();
                    throw e;
                }
            } catch (Exception e) {
                dbSpan.captureException(e);
                dbSpan.end();
                throw e;
            }
        } catch (Exception e) {
            transaction.captureException(e);
            return ResponseEntity.badRequest().body(null);
        } finally {
            transaction.end();
        }
    }

    @GetMapping("/isAble")
    public ResponseEntity<String> isAbleToReview(
            @RequestParam String username,
            @RequestParam String restaurantName) {
        Transaction transaction = ElasticApm.startTransaction();
        try (Scope txScope = transaction.activate()) {
            transaction.setName("HTTP GET /isAble");
            transaction.setType(Transaction.TYPE_REQUEST);

            Span businessLogicSpan = transaction.startSpan("custom", "logic", "isReservationTimePassed");
            try (Scope businessLogicScope = businessLogicSpan.activate()) {
                businessLogicSpan.setName("Is Reservation Time Passed");

                if (!reservationService.isReservationTimePassed(username, restaurantName)) {
                    return ResponseEntity.status(400).body("You need to have a past reservation to post a review");
                }
                businessLogicSpan.end();

                return ResponseEntity.ok().body("Able to review.");
            } catch (Exception e) {
                businessLogicSpan.captureException(e);
                businessLogicSpan.end();
                throw e;
            }
        } catch (Exception e) {
            transaction.captureException(e);
            return ResponseEntity.status(400).body("Failed: " + e.getMessage());
        } finally {
            transaction.end();
        }
    }

    @PostMapping("/cancelReservation")
    public ResponseEntity<String> cancelReservation(
            @RequestParam String username,
            @RequestParam String restaurantName,
            @RequestParam int tableNumber,
            @RequestParam int reservationNumber) {
        Transaction transaction = ElasticApm.startTransaction();
        try (Scope txScope = transaction.activate()) {
            transaction.setName("HTTP POST /cancelReservation");
            transaction.setType(Transaction.TYPE_REQUEST);

            Span dbSpan = transaction.startSpan("db", "query", "cancelReservation");
            try (Scope dbScope = dbSpan.activate()) {
                dbSpan.setName("Cancel Reservation");

                reservationRepository.cancelReservation(username, restaurantName, tableNumber, reservationNumber);
                dbSpan.end();

                return ResponseEntity.ok().body("Canceled successfully");
            } catch (Exception e) {
                dbSpan.captureException(e);
                dbSpan.end();
                throw e;
            }
        } catch (Exception e) {
            transaction.captureException(e);
            return ResponseEntity.badRequest().body("Failed to cancel reservation: " + e.getMessage());
        } finally {
            transaction.end();
        }
    }

    @PostMapping("/addReservation")
    public ResponseEntity<String> addReservation(
            @RequestParam String username,
            @RequestParam String restaurantName,
            @RequestParam int tableNumber,
            @RequestParam String date,
            @RequestParam String time) {
        Transaction transaction = ElasticApm.startTransaction();
        try (Scope txScope = transaction.activate()) {
            transaction.setName("HTTP POST /addReservation");
            transaction.setType(Transaction.TYPE_REQUEST);

            try {
                LocalDate reservationDate = LocalDate.parse(date);
                LocalTime reservationTime = LocalTime.parse(time);

                Span businessLogicSpan = transaction.startSpan("custom", "logic", "addReservation");
                try (Scope businessLogicScope = businessLogicSpan.activate()) {
                    businessLogicSpan.setName("Add Reservation");

                    reservationService.addReservation(username, restaurantName, tableNumber, reservationDate, reservationTime);
                    businessLogicSpan.end();

                    return ResponseEntity.ok().body("Reservation successfully added.");
                } catch (Exception e) {
                    businessLogicSpan.captureException(e);
                    businessLogicSpan.end();
                    throw e;
                }
            } catch (Exception e) {
                transaction.captureException(e);
                return ResponseEntity.status(400).body("Failed to add reservation: " + e.getMessage());
            }
        } finally {
            transaction.end();
        }
    }

    @GetMapping("/availableTimes")
    public ResponseEntity<AvailableTimeResponse> availableTimes(
            @RequestParam String restaurantName,
            @RequestParam int numberOfPeople,
            @RequestParam String date) {
        Transaction transaction = ElasticApm.startTransaction();
        try (Scope txScope = transaction.activate()) {
            transaction.setName("HTTP GET /availableTimes");
            transaction.setType(Transaction.TYPE_REQUEST);

            try {
                LocalDate selectedDate = LocalDate.parse(date);
                LocalDate today = LocalDate.now();
                LocalDate maxAllowedDate = today.plusDays(2);

                if (selectedDate.isAfter(maxAllowedDate)) {
                    return ResponseEntity.badRequest().body(null);
                }

                Span businessLogicSpan = transaction.startSpan("custom", "logic", "getAvailableTimes");
                try (Scope businessLogicScope = businessLogicSpan.activate()) {
                    businessLogicSpan.setName("Get Available Times");

                    Map<String, Object> availability = reservationService.getAvailableTimes(restaurantName, numberOfPeople, selectedDate);
                    List<Integer> availableTimes = (List<Integer>) availability.get("availableTimes");
                    TableEntity availableTable = (TableEntity) availability.get("availableTable");

                    businessLogicSpan.end();

                    if (availableTable == null) {
                        return ResponseEntity.notFound().build();
                    }

                    AvailableTimeResponse response = new AvailableTimeResponse(availableTimes, availableTable);
                    return ResponseEntity.ok(response);
                } catch (Exception e) {
                    businessLogicSpan.captureException(e);
                    businessLogicSpan.end();
                    throw e;
                }
            } catch (DateTimeParseException e) {
                transaction.captureException(e);
                return ResponseEntity.badRequest().body(null);
            } catch (Exception e) {
                transaction.captureException(e);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
            }
        } finally {
            transaction.end();
        }
    }

}
