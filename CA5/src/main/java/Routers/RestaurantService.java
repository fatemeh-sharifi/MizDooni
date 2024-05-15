package Routers;

import Entity.Feedback.FeedbackEntity;
import Entity.Restaurant.RestaurantEntity;
import Model.Address.AddressRestaurant;
import Model.Feedback.Feedback;
import Model.Restaurant.Restaurant;
import Repository.Address.AddressRestaurantRepository;
import Repository.Feedback.FeedbackRepository;
import Repository.Restaurant.RestaurantRepository;
import Repository.User.ClientRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
public class RestaurantService {


    @Autowired
    RestaurantRepository restaurantRepository;
    @Autowired
    FeedbackRepository feedbackRepository;
    @Autowired
    ClientRepository clientRepository;

    public List<Restaurant> getAllRestaurantsWithFeedbacks(List<RestaurantEntity> restaurantEntities) {
        List<Restaurant> restaurantModels = new ArrayList<>();

        for (RestaurantEntity restaurantEntity : restaurantEntities) {
            List<FeedbackEntity> feedbackEntities = feedbackRepository.findByRestaurantId(restaurantEntity.getId());
            List<Feedback> feedbackModels = new ArrayList<>();

            for (FeedbackEntity feedbackEntity : feedbackEntities) {
                Feedback feedbackModel = new Feedback();
                feedbackModel.setAmbianceRate(feedbackEntity.getAmbianceRate());
                feedbackModel.setServiceRate(feedbackEntity.getServiceRate());
                feedbackModel.setComment(feedbackEntity.getComment());
                feedbackModel.setRestaurantName(feedbackEntity.getRestaurant().getName());
                feedbackModel.setFoodRate(feedbackEntity.getFoodRate());
                feedbackModel.setDateTime(feedbackEntity.getDateTime());
                feedbackModel.setOverallRate(feedbackEntity.getOverallRate());
                feedbackModel.setUsername(feedbackEntity.getCustomer().getUsername());
                feedbackModels.add(feedbackModel);
            }

            Restaurant restaurantModel = new Restaurant();
            restaurantModel.setId(restaurantEntity.getId());
            restaurantModel.setServiceAvg(restaurantEntity.getServiceAvg());
            restaurantModel.setOverallAvg(restaurantEntity.getOverallAvg());
            restaurantModel.setFoodAvg(restaurantEntity.getFoodAvg());
            restaurantModel.setAmbianceAvg(restaurantEntity.getAmbianceAvg());
            restaurantModel.setDescription(restaurantEntity.getDescription());
            restaurantModel.setEndTime(restaurantEntity.getEndTime());
            restaurantModel.setStartTime(restaurantEntity.getStartTime());
            restaurantModel.setImage(restaurantEntity.getImage());
            restaurantModel.setManagerUsername(restaurantEntity.getName());
            restaurantModel.setType(restaurantEntity.getType());
            restaurantModel.setName(restaurantEntity.getName());
            restaurantModel.setReservations(new ArrayList<>());/////////////////
            restaurantModel.setTables(new ArrayList<>());////////////
            AddressRestaurant addressRestaurant = new AddressRestaurant();
            addressRestaurant.setCity(restaurantEntity.getAddress().getCity());
            addressRestaurant.setCountry(restaurantEntity.getAddress().getCountry());
            addressRestaurant.setStreet(restaurantEntity.getAddress().getStreet());
            restaurantModel.setAddress(addressRestaurant);
            restaurantModel.setFeedbacks(feedbackModels);
            restaurantModels.add(restaurantModel);
        }

        return restaurantModels;
    }
    @GetMapping("/restaurants")
    public ResponseEntity<List<Restaurant>> findRestaurants (
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String country,
            @RequestParam(required = false) String name
    ) {
        try {
            List<RestaurantEntity> filteredRestaurants = restaurantRepository.findRestaurants(type, city, country, name);
            List<Restaurant> restaurants = getAllRestaurantsWithFeedbacks(filteredRestaurants);
            return ResponseEntity.ok().body(restaurants);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/restaurants/{id}")
    public ResponseEntity<Restaurant> findRestaurantById ( @PathVariable int id ) {
        try {
            RestaurantEntity restaurant = restaurantRepository.findRestaurantById(id);
            if (restaurant != null) {
                List<RestaurantEntity> entity= new ArrayList<>();
                entity.add(restaurant);
                List<Restaurant> model = getAllRestaurantsWithFeedbacks(entity);
                Restaurant response = model.get(0);
                return ResponseEntity.ok().body(response);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/topRestaurants")
    public ResponseEntity<List<Restaurant>> findTopRestaurants (
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String country ) {
        try {
            List<RestaurantEntity> topRestaurants = restaurantRepository.findTopRestaurants(username, type, city, country);
            List<Restaurant> topRestaurantsModel = getAllRestaurantsWithFeedbacks(topRestaurants);
            List<Restaurant> top6RestaurantsModel = topRestaurantsModel.stream().limit(6).collect(Collectors.toList());
            return ResponseEntity.ok().body(top6RestaurantsModel);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
    @GetMapping("/types")
    public ResponseEntity<List<String>> findRestaurantsTypes(){
        try{
            List<String> types = restaurantRepository.findDistinctTypes();
            return ResponseEntity.ok().body(types);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
    @Autowired
    private AddressRestaurantRepository addressRestaurantRepository;

    public Map<String, List<String>> getCountriesAndCities() {
        List<Object[]> countryCityPairs = addressRestaurantRepository.findDistinctCountriesAndCities();
        Map<String, List<String>> countryCityMap = new HashMap<>();

        for (Object[] pair : countryCityPairs) {
            String country = (String) pair[0];
            String city = (String) pair[1];

            countryCityMap.computeIfAbsent(country, k -> new ArrayList<>()).add(city);
        }

        return countryCityMap;
    }
    @GetMapping("/location")
    public ResponseEntity<Map<String, List<String>>> findRestaurantsLocation() {
        try {
            Map<String, List<String>> countryCityMap = getCountriesAndCities();
            return ResponseEntity.ok().body(countryCityMap);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }

    }
    public Map<String, Map<String, List<String>>> getTypesCountriesAndCities() {
        List<String> types = restaurantRepository.findDistinctTypes();
        Map<String, Map<String, List<String>>> typeCountryCityMap = new HashMap<>();

        for (String type : types) {
            List<Object[]> countryCityPairs = addressRestaurantRepository.findDistinctCountriesAndCities();
            Map<String, List<String>> countryCityMap = new HashMap<>();

            for (Object[] pair : countryCityPairs) {
                String country = (String) pair[0];
                String city = (String) pair[1];

                countryCityMap.computeIfAbsent(country, k -> new ArrayList<>()).add(city);
            }

            typeCountryCityMap.put(type, countryCityMap);
        }

        return typeCountryCityMap;
    }
    @GetMapping("/typesCountriesAndCities")
    public ResponseEntity<Map<String, Map<String, List<String>>>> findTypesCountriesAndCities() {
        try {
            Map<String, Map<String, List<String>>> typeCountryCityMap = getTypesCountriesAndCities();
            return ResponseEntity.ok().body(typeCountryCityMap);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
    @Transactional
    public void updateRestaurantRatings(String restaurantName, Double foodRate, Double serviceRate,
                                        Double ambianceRate, Double overallRate) {
        try {
            RestaurantEntity restaurant = restaurantRepository.findByName(restaurantName);

            if (restaurant != null) {
                // Fetch feedbacks associated with the restaurant
                List<FeedbackEntity> feedbacks = feedbackRepository.findByRestaurantId(restaurant.getId());

                System.out.println(restaurant.getFoodAvg());
                // Update food, service, ambiance, and overall averages
                double newFoodAvg = calculateNewAverage(restaurant.getFoodAvg(), feedbacks.size(), foodRate);
                double newServiceAvg = calculateNewAverage(restaurant.getServiceAvg(), feedbacks.size(), serviceRate);
                double newAmbianceAvg = calculateNewAverage(restaurant.getAmbianceAvg(), feedbacks.size(), ambianceRate);
                double newOverallAvg = calculateOverallAverage(newFoodAvg, newServiceAvg, newAmbianceAvg);

                // Update restaurant averages
                restaurant.setFoodAvg(newFoodAvg);
                restaurant.setServiceAvg(newServiceAvg);
                restaurant.setAmbianceAvg(newAmbianceAvg);
                restaurant.setOverallAvg(newOverallAvg);

                // Save the updated restaurant entity
                restaurantRepository.save(restaurant);
            }
        } catch (Exception e) {
            // Handle any exceptions
            e.printStackTrace();
        }
    }

    // Helper method to calculate new average rating
    private double calculateNewAverage(double currentAvg, int currentCount, double newRating) {
        return (currentAvg * currentCount + newRating) / (currentCount + 1);
    }

    // Helper method to calculate overall average rating
    private double calculateOverallAverage(double foodAvg, double serviceAvg, double ambianceAvg) {
        // A simple average of the food, service, and ambiance averages
        return (foodAvg + serviceAvg + ambianceAvg) / 3.0;
    }
    @PostMapping("/reviews")
    public ResponseEntity<String> addOrUpdateReview(
            @RequestParam String username,
            @RequestParam String restaurantName,
            @RequestParam(required = false) Double foodRate,
            @RequestParam(required = false) Double serviceRate,
            @RequestParam(required = false) Double ambianceRate,
            @RequestParam(required = false) Double overallRate,
            @RequestParam(required = false) String comment
    ) {
        try {
            // Check if any review parameter is provided
            if (foodRate == null && serviceRate == null && ambianceRate == null && overallRate == null && comment == null) {
                return ResponseEntity.ok("Review parameters are valid, but review update not requested");
            }
            System.out.println(feedbackRepository.findAll().size());
            // Update feedback

            FeedbackEntity feedbackEntity = feedbackRepository.findByCustomerUsernameAndRestaurantName(username, restaurantName);
            if(feedbackEntity == null){
                LocalDateTime localDateTime = LocalDateTime.now();
                FeedbackEntity newFeedbackEntity = new FeedbackEntity(clientRepository.findByUsername(username), restaurantRepository.findByName(restaurantName), foodRate, serviceRate, ambianceRate,overallRate, comment, localDateTime);
                feedbackRepository.save(newFeedbackEntity);
                updateRestaurantRatings(restaurantName, foodRate, serviceRate, ambianceRate, overallRate);
            } else {
                // Update restaurant ratings if all parameters are provided
                if (foodRate != null && serviceRate != null && ambianceRate != null && overallRate != null && comment != null) {
                    feedbackRepository.updateExistingFeedback(username, restaurantName, foodRate, serviceRate, ambianceRate, overallRate, comment);
                    updateRestaurantRatings(restaurantName, foodRate, serviceRate, ambianceRate, overallRate);
                }
            }
            return ResponseEntity.ok("Review added/updated successfully");
        } catch (Exception e) {
            return ResponseEntity.status(400).body("Failed to add/update review: " + e.getMessage());
        }
    }

}
