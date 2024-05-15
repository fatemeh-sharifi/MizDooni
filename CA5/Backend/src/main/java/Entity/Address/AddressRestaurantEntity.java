package Entity.Address;

import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "restaurant_address")
public class AddressRestaurantEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "country", nullable = false)
    private String country;

    @Column(name = "city", nullable = false)
    private String city;

    @Column(name = "street")
    private String street;

    public AddressRestaurantEntity() {
    }

    public AddressRestaurantEntity(String country, String city, String street) {
        this.country = country;
        this.city = city;
        this.street = street;
    }
}
