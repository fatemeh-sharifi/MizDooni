package Service.DataFetch;

import DTO.Feedback.FeedbackDTO;
import DTO.Restaurant.RestaurantDTO;
import DTO.Table.TableDTO;
import Entity.Feedback.FeedbackEntity;
import Entity.Reservation.ReservationEntity;
import Entity.Restaurant.RestaurantEntity;
import Entity.Table.TableEntity;
import Entity.User.ClientEntity;
import Entity.User.ManagerEntity;
import Entity.User.UserEntity;
import Repository.Feedback.FeedbackRepository;
import Repository.Reservation.ReservationRepository;
import Repository.Restaurant.RestaurantRepository;
import Repository.Table.TableRepository;
import Repository.User.ClientRepository;
import Repository.User.ManagerRepository;
import Repository.User.UserRepository;
import Service.Feedback.FeedbackService;
import Service.User.UserService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
public class DataFetchService {

    private final UserRepository userRepository;
    private final ManagerRepository managerRepository;
    private final ClientRepository clientRepository;
    private final RestaurantRepository restaurantRepository;
    private final FeedbackRepository feedbackRepository;
    private final FeedbackService feedbackService;
    private final TableRepository tableRepository;
    private final ReservationRepository reservationRepository;
    private final UserService userService;
    @Autowired
    public DataFetchService( UserRepository userRepository, ManagerRepository managerRepository, ClientRepository clientRepository, RestaurantRepository restaurantRepository, FeedbackRepository feedbackRepository, FeedbackService feedbackService, TableRepository tableRepository, ReservationRepository reservationRepository , UserService userService ) {
        this.userRepository = userRepository;
        this.managerRepository = managerRepository;
        this.clientRepository = clientRepository;
        this.restaurantRepository = restaurantRepository;
        this.feedbackRepository = feedbackRepository;
        this.feedbackService = feedbackService;
        this.tableRepository = tableRepository;
        this.reservationRepository = reservationRepository;
        this.userService = userService;
    }

    public void fetchReservationsLocal(){

        ClientEntity userEntity = clientRepository.findByUsername("Mostafa_Ebrahimi");
        RestaurantEntity restaurantEntity = restaurantRepository.findByName("The Commoner");

        // First reservation
        LocalDate date1 = LocalDate.now().minusDays(2); // Assuming the reservation is for yesterday
        LocalTime time1 = LocalTime.of(15, 0);

        // Check if a reservation already exists for the given user, restaurant, date, and time
        ReservationEntity existingReservation1 = reservationRepository.findByUserAndRestaurantAndDateTime(
                userEntity.getUsername(), restaurantEntity.getName(), date1, time1);

        if (existingReservation1 == null) {
            ReservationEntity reservationEntity1 = new ReservationEntity();
            reservationEntity1.setUser(userEntity);
            reservationEntity1.setRestaurant(restaurantEntity);
            reservationEntity1.setDate(date1);
            reservationEntity1.setTime(time1);
            reservationEntity1.setCanceled(false); // Assuming the reservation is not canceled
            reservationEntity1.setTableSeat(4);
            long i = 1;
            reservationEntity1.setTable(tableRepository.findByIdAndRestaurantName(i, "The Commoner"));
            // Save the first reservationEntity using your ReservationRepository
            reservationRepository.save(reservationEntity1);
        } else {
            System.out.println("A reservation already exists for user Mostafa_Ebrahimi at The Commoner on " + date1 + " at " + time1);
        }

        // Second reservation
        LocalDate date2 = LocalDate.now().plusDays(1); // Assuming the reservation is for tomorrow
        LocalTime time2 = LocalTime.of(18, 0); // Set the minutes to zero

        // Check if a reservation already exists for the given user, restaurant, date, and time
        ReservationEntity existingReservation2 = reservationRepository.findByUserAndRestaurantAndDateTime(
                userEntity.getUsername(), restaurantEntity.getName(), date2, time2);

        if (existingReservation2 == null) {
            ReservationEntity reservationEntity2 = new ReservationEntity();
            reservationEntity2.setUser(userEntity);
            reservationEntity2.setRestaurant(restaurantEntity);
            reservationEntity2.setDate(date2);
            reservationEntity2.setTime(time2);
            reservationEntity2.setCanceled(false); // Assuming the reservation is not canceled
            // Save the second reservationEntity using your ReservationRepository
            reservationEntity2.setTableSeat(5);
            long i = 2;
            reservationEntity2.setTable(tableRepository.findByIdAndRestaurantName(i, reservationEntity2.getRestaurant().getName()));
            reservationRepository.save(reservationEntity2);
        } else {
            System.out.println("A reservation already exists for user Mostafa_Ebrahimi at The Commoner on " + date2 + " at " + time2);
        }


    }
    public void fetchUsersAndRestaurantsFromApi() {
        fetchAndSaveUsersFromApi();
        fetchAndSaveRestaurantsFromApi();
        fetchFeedbacksFromApi();
        fetchAndSaveTablesFromApi();
        fetchReservationsLocal();
        System.out.println("__________________ FETCHING IS FINISHED_________________");
    }

    private void fetchAndSaveTablesFromApi(){
        try{
            CloseableHttpClient httpClient = HttpClients.createDefault();
            String tablesEndpoint = "http://91.107.137.117:55/tables";
            CloseableHttpResponse tablesResponse = httpClient.execute(new HttpGet(tablesEndpoint));
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                List<TableDTO> fetchedTables = objectMapper.readValue(tablesResponse.getEntity().getContent(), new TypeReference<>() {});
                for (TableDTO fetchedTable : fetchedTables) {
                    TableEntity tableEntity = new TableEntity();
                    tableEntity.setManager(managerRepository.findByUsername(fetchedTable.getManagerUsername()));
                    tableEntity.setRestaurant(restaurantRepository.findByName(fetchedTable.getRestaurantName()));
                    tableEntity.setSeatsNumber(fetchedTable.getSeatsNumber());
                    saveTable(tableEntity);
                }
            } finally {
                tablesResponse.close();
            }
        } catch (IOException e) {
            throw new RuntimeException("Error fetching tables from API: " + e.getMessage());
        }
    }
    private void saveTable(TableEntity fetchedTable) {
        TableEntity existingTable = tableRepository.findByIdAndRestaurantName(fetchedTable.getId(), fetchedTable.getRestaurant().getName());
        if (existingTable == null) {
            tableRepository.save(fetchedTable);
        } else {
            // If the restaurant already exists, you may want to update it instead of saving again
            // For simplicity, I'm just printing a message here
            System.out.println("Table already exists: " + fetchedTable.getId());
        }
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
                client.setPassword(userService.hashPassword(fetchedUser.getPassword()));
                client.setEmail(fetchedUser.getEmail());
                client.setRole(fetchedUser.getRole());
                client.setAddress(fetchedUser.getAddress());
                clientRepository.save(client);
            } else if (fetchedUser.getRole().equals("manager")) {
                ManagerEntity manager = new ManagerEntity();
                manager.setUsername(fetchedUser.getUsername());
                manager.setPassword(userService.hashPassword(fetchedUser.getPassword()));
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

                    saveFeedback(fetchedFeedback);
                }
            } finally {
                feedbacksResponse.close();
            }
        } catch (IOException e) {
            throw new RuntimeException("Error fetching feedbacks from API: " + e.getMessage());
        }
    }

    private void saveFeedback(FeedbackDTO fetchedFeedback) {
        // Check if the feedback already exists in the database
        FeedbackEntity existingFeedback = feedbackRepository.findByCustomerUsernameAndRestaurantName(
                fetchedFeedback.getUsername(), fetchedFeedback.getRestaurantName());
        if (existingFeedback == null) {
            // If the feedback doesn't exist in the database, save it
            FeedbackEntity feedbackEntity = new FeedbackEntity();
            feedbackEntity.setCustomer(clientRepository.findByUsername(fetchedFeedback.getUsername()));
            feedbackEntity.setComment(fetchedFeedback.getComment());
            feedbackEntity.setAmbianceRate(fetchedFeedback.getAmbianceRate());
            // Fetch the restaurant entity by its name
            RestaurantEntity restaurantEntity = restaurantRepository.findByName(fetchedFeedback.getRestaurantName());
            // Set the fetched feedback's restaurant reference
            feedbackEntity.setRestaurant(restaurantEntity);
            feedbackEntity.setServiceRate(fetchedFeedback.getServiceRate());
            feedbackEntity.setDateTime(fetchedFeedback.getDateTime());
            feedbackEntity.setFoodRate(fetchedFeedback.getFoodRate());
            feedbackEntity.setAmbianceRate(fetchedFeedback.getAmbianceRate());
            feedbackEntity.setOverallRate(fetchedFeedback.getOverallRate());

            LocalDateTime localDateTime = LocalDateTime.now();
            feedbackEntity.setDateTime(localDateTime);
            feedbackRepository.save(feedbackEntity);
            feedbackService.updateRestaurantAverages(feedbackEntity);
        } else {
            // If the feedback already exists, you may want to update it instead of saving again
            // For simplicity, I'm just printing a message here
            System.out.println("Feedback already exists: " + fetchedFeedback.getId());
        }
    }

}
