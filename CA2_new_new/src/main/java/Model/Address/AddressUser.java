package Model.Address;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddressUser {
    private String country;
    private String city;

    public AddressUser(String country, String city) {
        this.country = country;
        this.city = city;
    }
}
