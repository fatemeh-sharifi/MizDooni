package Repository.Address;

import Entity.Address.AddressRestaurantEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AddressRestaurantRepository extends JpaRepository<AddressRestaurantEntity, Long> {
    @Query("SELECT DISTINCT a.country, a.city FROM AddressRestaurantEntity a")
    List<Object[]> findDistinctCountriesAndCities();

}
