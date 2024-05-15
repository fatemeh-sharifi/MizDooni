package Repository.Feedback;

import Entity.Feedback.FeedbackEntity;
import Entity.Restaurant.RestaurantEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FeedbackRepository extends JpaRepository<FeedbackEntity, Long> {
    FeedbackEntity findById(long id);
    FeedbackEntity findByCustomerUsernameAndRestaurantName(String username, String restaurantName);

    List<FeedbackEntity> findByRestaurant ( RestaurantEntity restaurant );
}
