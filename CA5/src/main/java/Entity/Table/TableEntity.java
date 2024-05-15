package Entity.Table;

import Entity.Restaurant.RestaurantEntity;
import Entity.User.ManagerEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "tables")
public class TableEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private long id;

    @ManyToOne
    @JoinColumn(name = "restaurant_name", nullable = false)
    private RestaurantEntity restaurant;

    @ManyToOne
    @JoinColumn(name = "manager_username", nullable = false)
    private ManagerEntity manager;

    @Column(name = "seats_number", nullable = false)
    private int seatsNumber;

    public TableEntity(){}

    public TableEntity(RestaurantEntity restaurant, ManagerEntity manager, int seatsNumber) {
        this.restaurant = restaurant;
        this.manager = manager;
        this.seatsNumber = seatsNumber;

    }
}
