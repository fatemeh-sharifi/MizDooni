package Entity.User;

import Entity.Address.AddressUserEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "users")
@Inheritance(strategy = InheritanceType.JOINED)
//@DiscriminatorColumn(name = "user_type", discriminatorType = DiscriminatorType.STRING)
public class UserEntity {

//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "id")
//    private Long id;

    @Id
    @Column(name = "username", unique = true, nullable = false)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @Column(name = "role", nullable = false)
    private String role;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id", referencedColumnName = "id")
    private AddressUserEntity address;

    public UserEntity ( String username, String email, String password, String role, AddressUserEntity addressUser ) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = role;
        this.address = addressUser;
    }

    public UserEntity ( ) {

    }
}

