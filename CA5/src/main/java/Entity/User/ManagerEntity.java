package Entity.User;

import Entity.Address.AddressUserEntity;
import Entity.Table.TableEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "managers")
@PrimaryKeyJoinColumn(name = "username")
public class ManagerEntity extends UserEntity {
    @OneToMany(mappedBy = "manager")
    private List<TableEntity> tables;

    public ManagerEntity ( String username, String email, String password, String role, AddressUserEntity addressUser ) {
        setUsername(username);
        setEmail(email);
        setPassword(password);
        setRole(role);
        setAddress(addressUser);
        this.tables = new ArrayList<>();/////////
    }

    public ManagerEntity ( ) {
        this.tables = new ArrayList<>();
    }
    // Additional manager-specific attributes
}
