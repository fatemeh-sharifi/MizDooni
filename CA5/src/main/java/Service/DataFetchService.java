package Service;

import DTO.Restaurant.RestaurantDTO;
import Entity.Restaurant.RestaurantEntity;
import Entity.User.ClientEntity;
import Entity.User.ManagerEntity;
import Entity.User.UserEntity;
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

    @Autowired
    public DataFetchService(UserRepository userRepository, ManagerRepository managerRepository, ClientRepository clientRepository, RestaurantRepository restaurantRepository) {
        this.userRepository = userRepository;
        this.managerRepository = managerRepository;
        this.clientRepository = clientRepository;
        this.restaurantRepository = restaurantRepository;
    }

    public void fetchUsersAndRestaurantsFromApi() {
        fetchAndSaveUsersFromApi();
        fetchAndSaveRestaurantsFromApi();
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
}
