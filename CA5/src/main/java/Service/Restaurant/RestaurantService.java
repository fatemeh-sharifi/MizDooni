//package Service.Restaurant;
//
//import DTO.Restaurant.RestaurantDTO;
//import Entity.Restaurant.RestaurantEntity;
//import Repository.Restaurant.RestaurantRepository;
//import Repository.User.ManagerRepository;
//import com.fasterxml.jackson.core.type.TypeReference;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.apache.http.client.methods.CloseableHttpResponse;
//import org.apache.http.client.methods.HttpGet;
//import org.apache.http.impl.client.CloseableHttpClient;
//import org.apache.http.impl.client.HttpClients;
//import org.springframework.stereotype.Service;
//
//import java.io.IOException;
//import java.util.List;
//
//@Service
//public class RestaurantService {
//
//    private final RestaurantRepository restaurantRepository;
//    private final ManagerRepository managerRepository;
//    private final String HOST = "http://91.107.137.117:55";
//    private final String RESTAURANTS_ENDPOINT = "/restaurants";
//
//    public RestaurantService ( RestaurantRepository restaurantRepository, ManagerRepository managerRepository ) {
//        this.restaurantRepository = restaurantRepository;
//        this.managerRepository = managerRepository;
//        this.fetchAndSaveRestaurantsFromApi();
//    }
//
//    public void fetchAndSaveRestaurantsFromApi ( ) {
//        try {
//            CloseableHttpClient httpClient = HttpClients.createDefault();
//            String restaurantsEndpoint = HOST + RESTAURANTS_ENDPOINT;
//            CloseableHttpResponse restaurantsResponse = httpClient.execute(new HttpGet(restaurantsEndpoint));
//            try {
//                ObjectMapper objectMapper = new ObjectMapper();
//                List<RestaurantDTO> fetchedRestaurants = objectMapper.readValue(restaurantsResponse.getEntity().getContent(), new TypeReference<List<RestaurantDTO>>() {
//                });
//                for (RestaurantDTO fetchedRestaurant : fetchedRestaurants) {
//                    fetchedRestaurant.generateId();
//                    RestaurantEntity existingRestaurant = restaurantRepository.findById(fetchedRestaurant.getId());
//                    if (existingRestaurant == null) {
//
//                        RestaurantEntity restaurantEntity = new RestaurantEntity();
////                        restaurantEntity.setId(fetchedRestaurant.getId());
//                        restaurantEntity.setImage(fetchedRestaurant.getImage());
//                        restaurantEntity.setName(fetchedRestaurant.getName());
//                        restaurantEntity.setAddress(fetchedRestaurant.getAddress());
//                        System.out.println(managerRepository.count() == 0);
//                        restaurantEntity.setManager(managerRepository.findByUsername(fetchedRestaurant.getManagerUsername()));
//                        restaurantEntity.setDescription(fetchedRestaurant.getDescription());
//                        restaurantEntity.setType(fetchedRestaurant.getType());
//                        restaurantEntity.setEndTime(fetchedRestaurant.getEndTime());
//                        restaurantEntity.setStartTime(fetchedRestaurant.getStartTime());
//                        restaurantEntity.generateId();
//                        restaurantRepository.save(restaurantEntity);
//                    } else {
//                        // Handle existing user case if needed
//                        // For example, update existing user details
//                        // existingUser.setName(fetchedUser.getName());
//                        // userRepository.save(existingUser);
//                    }
//                }
//            } finally {
//                restaurantsResponse.close();
//            }
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//}
