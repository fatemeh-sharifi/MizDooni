package Service.Restaurant;

import Entity.Restaurant.RestaurantEntity;
import Repository.Restaurant.RestaurantRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;
    private final String HOST = "http://91.107.137.117:55";
    private final String RESTAURANTS_ENDPOINT = "/restaurants";

    public RestaurantService(RestaurantRepository restaurantRepository) {
        this.restaurantRepository = restaurantRepository;
        this.fetchAndSaveRestaurantsFromApi();
    }

    public void fetchAndSaveRestaurantsFromApi() {
        try {
            CloseableHttpClient httpClient = HttpClients.createDefault();
            String restaurantsEndpoint = HOST + RESTAURANTS_ENDPOINT;
            CloseableHttpResponse restaurantsResponse = httpClient.execute(new HttpGet(restaurantsEndpoint));
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                List<RestaurantEntity> fetchedRestaurants = objectMapper.readValue(restaurantsResponse.getEntity().getContent(), new TypeReference<List<RestaurantEntity>>() {});
                restaurantRepository.saveAll(fetchedRestaurants);
            } finally {
                restaurantsResponse.close();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
