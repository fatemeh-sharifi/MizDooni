package Repository.Address;

import Entity.Address.AddressRestaurantEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressRestaurantRepository extends JpaRepository<AddressRestaurantEntity, Long> {
}
