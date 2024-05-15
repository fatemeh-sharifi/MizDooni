package Service.Feedback;

import Entity.Feedback.FeedbackEntity;
import Entity.Restaurant.RestaurantEntity;
import Repository.Feedback.FeedbackRepository;
import Repository.Restaurant.RestaurantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class FeedbackService {

    private final FeedbackRepository feedbackRepository;
    private final RestaurantRepository restaurantRepository;

    @Autowired
    public FeedbackService(FeedbackRepository feedbackRepository, RestaurantRepository restaurantRepository) {
        this.feedbackRepository = feedbackRepository;
        this.restaurantRepository = restaurantRepository;
    }

    public void updateRestaurantAverages( FeedbackEntity feedback) {
        RestaurantEntity restaurant = feedback.getRestaurant();
        if (restaurant != null) {
            // Calculate averages for food, service, ambiance, overall
            List<FeedbackEntity> feedbacks = feedbackRepository.findByRestaurant(restaurant);
            double foodSum = 0.0;
            double serviceSum = 0.0;
            double ambianceSum = 0.0;
            double overallSum = 0.0;
            int count = feedbacks.size();

            for (FeedbackEntity f : feedbacks) {
                foodSum += f.getFoodRate();
                serviceSum += f.getServiceRate();
                ambianceSum += f.getAmbianceRate();
                overallSum += f.getOverallRate();
            }

            double foodAvg = foodSum / count;
            double serviceAvg = serviceSum / count;
            double ambianceAvg = ambianceSum / count;
            double overallAvg = overallSum / count;

            // Update restaurant averages
            restaurant.setFoodAvg(foodAvg);
            restaurant.setServiceAvg(serviceAvg);
            restaurant.setAmbianceAvg(ambianceAvg);
            restaurant.setOverallAvg(overallAvg);

            // Save the updated restaurant
            restaurantRepository.save(restaurant);
        }
    }
}
