package model;

public class AddressUser {
    private String country;
    private String city;

    public AddressUser(String country, String city, String street) {
        this.country = country;
        this.city = city;
    }

    // Getters and setters for attributes

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
