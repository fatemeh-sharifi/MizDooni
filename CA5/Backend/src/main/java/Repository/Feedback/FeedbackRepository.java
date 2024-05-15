package Repository.Feedback;

import Entity.Feedback.FeedbackEntity;
import Entity.Restaurant.RestaurantEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FeedbackRepository extends JpaRepository<FeedbackEntity, Long> {
    FeedbackEntity findById(long id);
    FeedbackEntity findByCustomerUsernameAndRestaurantName(String username, String restaurantName);

    List<FeedbackEntity> findByRestaurant ( RestaurantEntity restaurant );

    List<FeedbackEntity> findByRestaurantId ( int id );

    @Modifying
    @Query("UPDATE FeedbackEntity f SET f.foodRate = :foodRate, f.serviceRate = :serviceRate, f.ambianceRate = :ambianceRate, " +
            "f.overallRate = :overallRate, f.comment = :comment " +
            "WHERE f.customer.username = :username AND f.restaurant.name = :restaurantName")
    void updateExistingFeedback(@Param("username") String username, @Param("restaurantName") String restaurantName,
                                @Param("foodRate") Double foodRate, @Param("serviceRate") Double serviceRate,
                                @Param("ambianceRate") Double ambianceRate, @Param("overallRate") Double overallRate,
                                @Param("comment") String comment);






}
