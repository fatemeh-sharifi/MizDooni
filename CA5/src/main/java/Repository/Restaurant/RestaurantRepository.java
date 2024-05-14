package Repository.Restaurant;

import Entity.Restaurant.RestaurantEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RestaurantRepository extends JpaRepository<RestaurantEntity, Long> {

    List<RestaurantEntity> findByTypeIgnoreCase(String type);

    List<RestaurantEntity> findByAddress_CityIgnoreCase(String city);

    List<RestaurantEntity> findByAddress_CountryIgnoreCase(String country);

    List<RestaurantEntity> findByNameIgnoreCase(String name);

    @Query("SELECT r FROM RestaurantEntity r " +
            "WHERE (:type IS NULL OR LOWER(r.type) = LOWER(:type)) " +
            "AND (:city IS NULL OR LOWER(r.address.city) = LOWER(:city)) " +
            "AND (:country IS NULL OR LOWER(r.address.country) = LOWER(:country)) " +
            "AND (:name IS NULL OR LOWER(r.name) = LOWER(:name))")
    List<RestaurantEntity> findByFilters(String type, String city, String country, String name);
}
