//package Repository.Restaurant;
//
//import Entity.Restaurant.RestaurantEntity;
//import DAO.Restaurant.RestaurantDAO;
//import Entity.User.UserEntity;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.stereotype.Repository;
//
//import java.util.List;
//
//@Repository
//public interface RestaurantRepository extends JpaRepository<RestaurantEntity, Long> {
//    private final RestaurantDAO restaurantDAO;
//
//    @Autowired
//    public RestaurantRepository(RestaurantDAO restaurantDAO) {
//        this.restaurantDAO = restaurantDAO;
//    }
//
//    public void saveOrUpdate(RestaurantEntity restaurant) {
//        restaurantDAO.saveOrUpdate(restaurant);
//    }
//
//    public RestaurantEntity getById(int id) {
//        return restaurantDAO.getById(id);
//    }
//
//    public void delete(RestaurantEntity restaurant) {
//        restaurantDAO.delete(restaurant);
//    }
//
//    public List<RestaurantEntity> getAllRestaurants() {
//        return restaurantDAO.getAllRestaurants();
//    }
//
//    public List<RestaurantEntity> findRestaurants(String type, String city, String country, String name) {
//        return restaurantDAO.findRestaurants(type, city, country, name);
//    }
//}
package Repository.Restaurant;

import Entity.Restaurant.RestaurantEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RestaurantRepository extends JpaRepository<RestaurantEntity, Long> {
    RestaurantEntity findById(int id);
//    List<RestaurantEntity> findRestaurants( String type, String city, String country, String name);
    @Query("SELECT r FROM RestaurantEntity r WHERE " +
            "(:type IS NULL OR r.type = :type) AND " +
            "(:city IS NULL OR r.address.city = :city) AND " +
            "(:country IS NULL OR r.address.country = :country) AND " +
            "(:name IS NULL OR r.name = :name)")
    List<RestaurantEntity> findRestaurants(
            String type, String city, String country, String name);

    @Query("SELECT r FROM RestaurantEntity r WHERE r.id = :id")
    RestaurantEntity findRestaurantById(@Param("id") int id);
}

