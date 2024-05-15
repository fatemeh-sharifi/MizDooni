package Service;

import DTO.Feedback.FeedbackDTO;
import DTO.Restaurant.RestaurantDTO;
import Entity.Feedback.FeedbackEntity;
import Entity.Restaurant.RestaurantEntity;
import Entity.User.ClientEntity;
import Entity.User.ManagerEntity;
import Entity.User.UserEntity;
import Repository.Feedback.FeedbackRepository;
import Repository.Restaurant.RestaurantRepository;
import Repository.User.ClientRepository;
import Repository.User.ManagerRepository;
import Repository.User.UserRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class DataFetchService {

    private final UserRepository userRepository;
    private final ManagerRepository managerRepository;
    private final ClientRepository clientRepository;
    private final RestaurantRepository restaurantRepository;
    private final FeedbackRepository feedbackRepository;

    @Autowired
    public DataFetchService(UserRepository userRepository, ManagerRepository managerRepository, ClientRepository clientRepository, RestaurantRepository restaurantRepository, FeedbackRepository feedbackRepository) {
        this.userRepository = userRepository;
        this.managerRepository = managerRepository;
        this.clientRepository = clientRepository;
        this.restaurantRepository = restaurantRepository;
        this.feedbackRepository = feedbackRepository;
    }

    public void fetchUsersAndRestaurantsFromApi() {
        fetchAndSaveUsersFromApi();
        fetchAndSaveRestaurantsFromApi();
        fetchFeedbacksFromApi();
    }

    private void fetchAndSaveUsersFromApi() {
        try {
            CloseableHttpClient httpClient = HttpClients.createDefault();
            String usersEndpoint = "http://91.107.137.117:55/users";
            CloseableHttpResponse usersResponse = httpClient.execute(new HttpGet(usersEndpoint));
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                List<UserEntity> fetchedUsers = objectMapper.readValue(usersResponse.getEntity().getContent(), new TypeReference<>() {});
                for (UserEntity fetchedUser : fetchedUsers) {
                    saveUser(fetchedUser);
                }
            } finally {
                usersResponse.close();
            }
        } catch (IOException e) {
            throw new RuntimeException("Error fetching users from API: " + e.getMessage());
        }
    }

    private void saveUser(UserEntity fetchedUser) {
        UserEntity existingUser = userRepository.findByUsername(fetchedUser.getUsername());
        if (existingUser == null) {
            if (fetchedUser.getRole().equals("client")) {
                ClientEntity client = new ClientEntity();
                client.setUsername(fetchedUser.getUsername());
                client.setPassword(fetchedUser.getPassword());
                client.setEmail(fetchedUser.getEmail());
                client.setRole(fetchedUser.getRole());
                client.setAddress(fetchedUser.getAddress());
                clientRepository.save(client);
            } else if (fetchedUser.getRole().equals("manager")) {
                ManagerEntity manager = new ManagerEntity();
                manager.setUsername(fetchedUser.getUsername());
                manager.setPassword(fetchedUser.getPassword());
                manager.setEmail(fetchedUser.getEmail());
                manager.setRole(fetchedUser.getRole());
                manager.setAddress(fetchedUser.getAddress());
                managerRepository.save(manager);
            }
        }
    }
    private void fetchAndSaveRestaurantsFromApi() {
        try {
            CloseableHttpClient httpClient = HttpClients.createDefault();
            String restaurantsEndpoint = "http://91.107.137.117:55/restaurants";
            CloseableHttpResponse restaurantsResponse = httpClient.execute(new HttpGet(restaurantsEndpoint));
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                List<RestaurantDTO> fetchedRestaurants = objectMapper.readValue(restaurantsResponse.getEntity().getContent(), new TypeReference<List<RestaurantDTO>>() {});
                for (RestaurantDTO fetchedRestaurant : fetchedRestaurants) {
                    fetchedRestaurant.generateId();
                    RestaurantEntity existingRestaurant = restaurantRepository.findById(fetchedRestaurant.getId());
                    if (existingRestaurant == null) {
                        RestaurantEntity restaurantEntity = new RestaurantEntity();
//                        restaurantEntity.setId(fetchedRestaurant.getId());
                        restaurantEntity.setImage(fetchedRestaurant.getImage());
                        restaurantEntity.setName(fetchedRestaurant.getName());
                        restaurantEntity.setAddress(fetchedRestaurant.getAddress());
                        System.out.println(managerRepository.count() == 0);
                        restaurantEntity.setManager(managerRepository.findByUsername(fetchedRestaurant.getManagerUsername()));
                        restaurantEntity.setDescription(fetchedRestaurant.getDescription());
                        restaurantEntity.setType(fetchedRestaurant.getType());
                        restaurantEntity.setEndTime(fetchedRestaurant.getEndTime());
                        restaurantEntity.setStartTime(fetchedRestaurant.getStartTime());
                        restaurantEntity.generateId();
                        saveRestaurant(restaurantEntity);
                    } else {
                        // Handle existing restaurant case if needed
                        // For example, update existing restaurant details
                        // existingRestaurant.setName(fetchedRestaurant.getName());
                        // restaurantRepository.save(existingRestaurant);
                    }
                }
            } finally {
                restaurantsResponse.close();
            }
        } catch (IOException e) {
            throw new RuntimeException("Error fetching restaurants from API: " + e.getMessage());
        }
    }

    private void saveRestaurant(RestaurantEntity fetchedRestaurant) {
        RestaurantEntity existingRestaurant = restaurantRepository.findById(fetchedRestaurant.getId());
        if (existingRestaurant == null) {

            // If the restaurant doesn't exist in the database, save it
            restaurantRepository.save(fetchedRestaurant);
        } else {
            // If the restaurant already exists, you may want to update it instead of saving again
            // For simplicity, I'm just printing a message here
            System.out.println("Restaurant already exists: " + fetchedRestaurant.getName());
        }
    }
    public void fetchFeedbacksFromApi() {
        try {
            CloseableHttpClient httpClient = HttpClients.createDefault();
            String feedbacksEndpoint = "http://91.107.137.117:55/reviews"; // Replace with your actual API endpoint
            CloseableHttpResponse feedbacksResponse = httpClient.execute(new HttpGet(feedbacksEndpoint));
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                List<FeedbackDTO> fetchedFeedbacks = objectMapper.readValue(feedbacksResponse.getEntity().getContent(), new TypeReference<>() {});
                for (FeedbackDTO fetchedFeedback : fetchedFeedbacks) {

                    System.out.println(fetchedFeedback.getId());
                    System.out.println(fetchedFeedback.getRestaurantName());
                    saveFeedback(fetchedFeedback);
                }
            } finally {
                feedbacksResponse.close();
            }
        } catch (IOException e) {
            throw new RuntimeException("Error fetching feedbacks from API: " + e.getMessage());
        }
    }

    private void saveFeedback( FeedbackDTO fetchedFeedback) {
        // Check if the feedback already exists in the database
        FeedbackEntity existingFeedback = feedbackRepository.findByCustomerUsernameAndRestaurantName(
                fetchedFeedback.getUsername(), fetchedFeedback.getRestaurantName());
        if (existingFeedback == null) {
            // If the feedback doesn't exist in the database, save it
            FeedbackEntity feedbackEntity = new FeedbackEntity();
            feedbackEntity.setCustomer(clientRepository.findByUsername(fetchedFeedback.getUsername()));
            feedbackEntity.setComment(fetchedFeedback.getComment());
            feedbackEntity.setAmbianceRate(fetchedFeedback.getAmbianceRate());
            feedbackEntity.setRestaurant(restaurantRepository.findByName(fetchedFeedback.getRestaurantName()));
            feedbackEntity.setServiceRate(fetchedFeedback.getServiceRate());
            feedbackEntity.setDateTime(fetchedFeedback.getDateTime());
            feedbackEntity.setFoodRate(fetchedFeedback.getFoodRate());
            feedbackEntity.setAmbianceRate(fetchedFeedback.getAmbianceRate());
            feedbackEntity.setOverallRate(fetchedFeedback.getOverallRate());
            feedbackRepository.save(feedbackEntity);
        } else {
            // If the feedback already exists, you may want to update it instead of saving again
            // For simplicity, I'm just printing a message here
            System.out.println("Feedback already exists: " + fetchedFeedback.getId());
        }
    }
}
