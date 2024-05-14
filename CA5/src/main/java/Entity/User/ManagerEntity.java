package Entity.User;

import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;

@Entity
@Table(name = "managers")
@PrimaryKeyJoinColumn(name = "id")
public class ManagerEntity extends UserEntity {
    // Additional manager-specific attributes
}
