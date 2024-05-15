package Entity.User;

import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;

@Entity
@Table(name = "clients")
@PrimaryKeyJoinColumn(name = "username")
public class ClientEntity extends UserEntity {
    // Additional client-specific attributes
}
