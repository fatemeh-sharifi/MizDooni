package Service.Reservation;

import Entity.Reservation.ReservationEntity;
import Entity.Restaurant.RestaurantEntity;
import Entity.Table.TableEntity;
import Model.Reservation.Reservation;
import Repository.Reservation.ReservationRepository;
import Repository.Restaurant.RestaurantRepository;
import Repository.Table.TableRepository;
import Repository.User.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
@Service
public class ReservationService {
    @Autowired
    ReservationRepository reservationRepository;
    @Autowired
    ClientRepository clientRepository;
    @Autowired
    RestaurantRepository restaurantRepository;
    @Autowired
    TableRepository tableRepository;

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
    public Map<String, Object> getAvailableTimes(String restaurantName, int numberOfPeople, LocalDate selectedDate) {
        Map<String, Object> availability = new HashMap<>();
        try {
            // Retrieve the restaurant
            RestaurantEntity restaurant = restaurantRepository.findByName(restaurantName);
            if (restaurant == null) {
                return availability; // Return empty availability if restaurant not found
            }

            // Sort tables based on the difference between their capacity and the required number of people
            List<TableEntity> sortedTables = new ArrayList<>(tableRepository.findByRestaurantName(restaurantName));
            sortedTables.sort(Comparator.comparingInt(t -> Math.abs(t.getSeatsNumber() - numberOfPeople)));

            // Find the first available time for the sorted tables
            List<Integer> availableTimes = new ArrayList<>();
            TableEntity availableTable = null;

            // Get reservations for the selected date
            List<ReservationEntity> reservations = reservationRepository.findReservationsInRestaurantOnDate(restaurantName, selectedDate);

            for (TableEntity table : sortedTables) {
                String start = table.getRestaurant().getStartTime();
                String end = table.getRestaurant().getEndTime();
                String[] parts = start.split(":");
                int start_hour = Integer.parseInt(parts[0]);
                parts = end.split(":");
                int end_hour = Integer.parseInt(parts[0]);
                List<Integer> tableAvailableTimes = new ArrayList<>();
                for (int i = start_hour; i <= end_hour; i++) {
                    tableAvailableTimes.add(i);
                }

                // Remove times of reserved tables
                for (ReservationEntity reservation : reservations) {
                    if (reservation.getTable().getId() == table.getId()) {
                        tableAvailableTimes.remove(reservation.getTime().getHour());
                    }
                }

                if (!tableAvailableTimes.isEmpty()) {
                    availableTimes = tableAvailableTimes;
                    availableTable = table;
                    break;
                }
            }

            // Populate the availability map
            availability.put("availableTimes", availableTimes);
            availability.put("availableTable", availableTable);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return availability;
    }

}
