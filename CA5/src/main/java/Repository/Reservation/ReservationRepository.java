package Repository.Reservation;

import Entity.Reservation.ReservationEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<ReservationEntity, Long> {
    @Query("SELECT r FROM ReservationEntity r WHERE r.user.username = :username " +
            "AND r.restaurant.name = :restaurantName " +
            "AND r.date = :date " +
            "AND r.time = :time")
    ReservationEntity findByUserAndRestaurantAndDateTime(
            @Param("username") String username,
            @Param("restaurantName") String restaurantName,
            @Param("date") LocalDate date,
            @Param("time") LocalTime time);
    @Query("SELECT r FROM ReservationEntity r WHERE r.user.username = :username")
    List<ReservationEntity> findUserReservations( @Param("username") String username);
    @Query("SELECT r FROM ReservationEntity r " +
            "WHERE r.user.username = :username " +
            "AND r.restaurant.name = :restaurantName " +
            "AND r.date < :currentDate " +
            "OR (r.date = :currentDate AND r.time < :currentTime)")
    List<ReservationEntity> findPastReservations(
            @Param("username") String username,
            @Param("restaurantName") String restaurantName,
            @Param("currentDate") LocalDate currentDate,
            @Param("currentTime") LocalTime currentTime
    );
    @Modifying
    @Transactional
    @Query("DELETE FROM ReservationEntity r WHERE r.user.username = :username AND r.restaurant.name = :restaurantName AND r.table.id = :tableNumber AND r.reservationNumber = :reservationNumber")
    void cancelReservation(@Param("username") String username, @Param("restaurantName") String restaurantName, @Param("tableNumber") int tableNumber, @Param("reservationNumber") int reservationNumber);
}

