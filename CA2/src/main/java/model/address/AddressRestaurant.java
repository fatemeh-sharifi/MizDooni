package model.address;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddressRestaurant {
    private String country;
    private String city;
    private String street;

    public AddressRestaurant(String country, String city, String street) {
        this.country = country;
        this.city = city;
        this.street = street;
    }
}
