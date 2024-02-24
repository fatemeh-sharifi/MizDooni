package model;

import java.util.List;

public class User {
    private String username;
    private String email;
    private String password;
    private List<Reservation> reservations;
    private AddressUser address;
    public User(String username, String email, String password, AddressUser address) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.address = address;
    }

    // Getters and setters for attributes

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Reservation> getReservations() {
        return reservations;
    }

    public void setReservations(List<Reservation> reservations) {
        this.reservations = reservations;
    }

    public void setAddress(AddressUser address) {
        this.address = address;
    }

    public AddressUser getAddress() {
        return address;
    }

}
