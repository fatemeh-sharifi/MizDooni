package domain.user;

import java.util.ArrayList;
import java.util.List;

import domain.address.AddressUser;
import domain.reservation.Reservation;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class User {
    private String username;
    String role;
    private String email;
    private String password;
    private List<Reservation> reservations;
    private AddressUser address;
    public User(String username, String email, String password, String role, AddressUser address) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.address = address;
        this.role = role;
        this.reservations = new ArrayList<>();
    }
    public void addReservation(Reservation reservation){
        reservations.add(reservation);
    }
}
