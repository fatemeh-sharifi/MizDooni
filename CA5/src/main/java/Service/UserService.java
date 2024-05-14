package Service;

import Entity.User.UserEntity;
import Repository.User.UserRepository;
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
public class UserService {

    private final UserRepository userRepository;
    private final String HOST = "http://91.107.137.117:55";
    private final String USERS_ENDPOINT = "/users";

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.fetchAndSaveUsersFromApi();
    }

    public void fetchAndSaveUsersFromApi() {
        try {
            CloseableHttpClient httpClient = HttpClients.createDefault();
            String usersEndpoint = "http://91.107.137.117:55/users";
            CloseableHttpResponse usersResponse = httpClient.execute(new HttpGet(usersEndpoint));
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                List<UserEntity> fetchedUsers = objectMapper.readValue(usersResponse.getEntity().getContent(), new TypeReference<List<UserEntity>>() {
                });
                userRepository.saveAll(fetchedUsers);
            } finally {
                usersResponse.close();
            }
//            String usersString = Request.makeGetRequest(HOST + USERS_ENDPOINT);
//            ObjectMapper objectMapper = new ObjectMapper();
//            List<UserEntity> userList = objectMapper.readValue(usersString, new TypeReference<>() {});
//            userRepository.saveAll(userList);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}