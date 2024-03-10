package Model.Address;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddressRestaurant {
    private String country;
    private String city;
    private String street;

    public AddressRestaurant() {
    }
    public AddressRestaurant(String country, String city, String street) {
        this.country = country;
        this.city = city;
        this.street = street;
    }
}
