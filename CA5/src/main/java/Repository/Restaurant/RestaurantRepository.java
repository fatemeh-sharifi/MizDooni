//package Repository.Restaurant;
//
//import Entity.Restaurant.RestaurantEntity;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Query;
//
//import java.util.List;
//
//public interface RestaurantRepository extends JpaRepository<RestaurantEntity, Long> {
//
//    List<RestaurantEntity> findByTypeIgnoreCase(String type);
//
//    List<RestaurantEntity> findByAddress_CityIgnoreCase(String city);
//
//    List<RestaurantEntity> findByAddress_CountryIgnoreCase(String country);
//
//    List<RestaurantEntity> findByNameIgnoreCase(String name);
//
//    @Query("SELECT r FROM RestaurantEntity r " +
//            "WHERE (:type IS NULL OR LOWER(r.type) = LOWER(:type)) " +
//            "AND (:city IS NULL OR LOWER(r.address.city) = LOWER(:city)) " +
//            "AND (:country IS NULL OR LOWER(r.address.country) = LOWER(:country)) " +
//            "AND (:name IS NULL OR LOWER(r.name) = LOWER(:name))")
//    List<RestaurantEntity> findByFilters(String type, String city, String country, String name);
//}
package Repository.Restaurant;

import Entity.Restaurant.RestaurantEntity;
import DAO.Restaurant.RestaurantDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class RestaurantRepository {
    private final RestaurantDAO restaurantDAO;

    @Autowired
    public RestaurantRepository(RestaurantDAO restaurantDAO) {
        this.restaurantDAO = restaurantDAO;
    }

    public void saveOrUpdate(RestaurantEntity restaurant) {
        restaurantDAO.saveOrUpdate(restaurant);
    }

    public RestaurantEntity getById(int id) {
        return restaurantDAO.getById(id);
    }

    public void delete(RestaurantEntity restaurant) {
        restaurantDAO.delete(restaurant);
    }

    public List<RestaurantEntity> getAllRestaurants() {
        return restaurantDAO.getAllRestaurants();
    }

    public List<RestaurantEntity> findRestaurants(String type, String city, String country, String name) {
        return restaurantDAO.findRestaurants(type, city, country, name);
    }
}
