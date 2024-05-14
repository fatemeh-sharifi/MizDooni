package Service.User;

import Entity.User.ClientEntity;
import Entity.User.ManagerEntity;
import Entity.User.UserEntity;
import Repository.User.ClientRepository;
import Repository.User.ManagerRepository;
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
    private final ManagerRepository managerRepository;
    private final ClientRepository clientRepository;
    private final String HOST = "http://91.107.137.117:55";
    private final String USERS_ENDPOINT = "/users";

    public UserService( UserRepository userRepository, ManagerRepository managerRepository, ClientRepository clientRepository ) {
        this.userRepository = userRepository;
        this.managerRepository = managerRepository;
        this.clientRepository = clientRepository;
        this.fetchAndSaveUsersFromApi();
    }

    public void fetchAndSaveUsersFromApi() {
        try {
            CloseableHttpClient httpClient = HttpClients.createDefault();
            String usersEndpoint = "http://91.107.137.117:55/users";
            CloseableHttpResponse usersResponse = httpClient.execute(new HttpGet(usersEndpoint));
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                List<UserEntity> fetchedUsers = objectMapper.readValue(usersResponse.getEntity().getContent(), new TypeReference<List<UserEntity>>() {});
                // Check if each fetched user already exists in the database before saving
                for (UserEntity fetchedUser : fetchedUsers) {
                    UserEntity existingUser = userRepository.findByUsername(fetchedUser.getUsername());
//                    ManagerEntity existingManager = managerRepository.findByUsername(fetchedUser.getUsername());
                    if (existingUser == null) {
                        //userRepository.save(fetchedUser);
                        if(fetchedUser.getRole().equals("client")){
                            ClientEntity existingClient = clientRepository.findByUsername(fetchedUser.getUsername());
                            if(existingClient == null){
                                ClientEntity client = new ClientEntity();
                                client.setUsername(fetchedUser.getUsername());
                                client.setPassword(fetchedUser.getPassword());
                                client.setEmail(fetchedUser.getEmail());
                                client.setRole(fetchedUser.getRole());
                                client.setAddress(fetchedUser.getAddress());
                                clientRepository.save(client);
                            }
                        } else if (fetchedUser.getRole().equals("manager")){
                            ManagerEntity existingManager = managerRepository.findByUsername(fetchedUser.getUsername());
                            if(existingManager == null){
                                ManagerEntity manager = new ManagerEntity();
                                manager.setUsername(fetchedUser.getUsername());
                                manager.setPassword(fetchedUser.getPassword());
                                manager.setEmail(fetchedUser.getEmail());
                                manager.setRole(fetchedUser.getRole());
                                manager.setAddress(fetchedUser.getAddress());
                                managerRepository.save(manager);
                            }
                        }
                    } else {
                        // Handle existing user case if needed
                        // For example, update existing user details
                        // existingUser.setName(fetchedUser.getName());
                        // userRepository.save(existingUser);
                    }
                }
            } finally {
                usersResponse.close();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
