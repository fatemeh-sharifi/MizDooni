package Entity.Address;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "user_address")
public class AddressUserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column()
    private String country;
    @Column()
    private String city;

    public AddressUserEntity() {
    }

    public AddressUserEntity(String country, String city) {
        this.country = country;
        this.city = city;
    }

}
