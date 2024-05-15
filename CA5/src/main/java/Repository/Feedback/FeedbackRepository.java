package Repository.Feedback;

import Entity.Feedback.FeedbackEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FeedbackRepository extends JpaRepository<FeedbackEntity, Long> {
    FeedbackEntity findById(long id);
    FeedbackEntity findByCustomerUsernameAndRestaurantName(String username, String restaurantName);
}
