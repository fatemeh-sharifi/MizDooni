package domain.address;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddressUser {
    private String country;
    private String city;
    // Default Constructor
    public AddressUser() {
    }
    public AddressUser(String country, String city) {
        this.country = country;
        this.city = city;
    }
}
