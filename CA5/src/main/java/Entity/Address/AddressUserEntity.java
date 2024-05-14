package Entity.Address;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class AddressUserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String country;
    private String city;

    public AddressUserEntity() {
    }

    public AddressUserEntity(String country, String city) {
        this.country = country;
        this.city = city;
    }

}
