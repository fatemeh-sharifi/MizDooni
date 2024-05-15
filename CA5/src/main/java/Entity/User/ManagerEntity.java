package Entity.User;

import Entity.Address.AddressUserEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;

@Entity
@Table(name = "managers")
@PrimaryKeyJoinColumn(name = "username")
public class ManagerEntity extends UserEntity {
    public ManagerEntity ( String username, String email, String password, String role, AddressUserEntity addressUser ) {
        setUsername(username);
        setEmail(email);
        setPassword(password);
        setRole(role);
        setAddress(addressUser);
    }

    public ManagerEntity ( ) {

    }
    // Additional manager-specific attributes
}
