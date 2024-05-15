package Entity.User;

import Entity.Address.AddressUserEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;

@Entity
@Table(name = "clients")
@PrimaryKeyJoinColumn(name = "username")
public class ClientEntity extends UserEntity {
    public ClientEntity ( String username, String email, String password, String role, AddressUserEntity addressUser ) {
        setUsername(username);
        setEmail(email);
        setPassword(password);
        setRole(role);
        setAddress(addressUser);
    }

    public ClientEntity ( ) {

    }
}
