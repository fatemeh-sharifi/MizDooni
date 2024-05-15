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
    RestaurantEntity findByName(String name);
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

//    @Query("SELECT r FROM RestaurantEntity r " +
//            "WHERE (:username IS NULL OR r.address.city = (SELECT u.address.city FROM UserEntity u WHERE u.username = :username)) " +
//            "AND (:username IS NULL OR r.address.country = (SELECT u.address.country FROM UserEntity u WHERE u.username = :username)) " +
//            "AND (:type IS NULL OR r.type = :type) " +
//            "AND (:city IS NULL OR r.address.city = :city) " +
//            "AND (:country IS NULL OR r.address.country = :country)")
//    List<RestaurantEntity> findRestaurants(
//            String username, String type, String city, String country);

//    @Query("SELECT r, AVG(f.overallRate) AS avgRating FROM RestaurantEntity r " +
//            "LEFT JOIN r.address a " +
//            "LEFT JOIN FeedbackEntity f ON r.name = f.restaurant.name " +
//            "LEFT JOIN UserEntity u ON u.address = a " +
//            "WHERE (:username IS NULL OR u.username = :username) " +
//            "AND (:type IS NULL OR r.type = :type) " +
//            "AND (:city IS NULL OR a.city = :city) " +
//            "AND (:country IS NULL OR a.country = :country) " +
//            "GROUP BY r " +
//            "ORDER BY avgRating DESC")
//    List<Object[]> findTopRestaurantsWithAvgRating(
//            @Param("username") String username,
//            @Param("type") String type,
//            @Param("city") String city,
//            @Param("country") String country,
//            Pageable pageable);
}

