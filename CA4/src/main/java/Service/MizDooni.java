package Service;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

import Controller.AuthenticationController;
import Model.Table.Table;
import Model.Address.AddressUser;
import Model.Exception.ExceptionMessages;
import Model.Exception.SuperException;
import Model.Reservation.Reservation;
import Model.Table.Table;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;
import Model.User.User;
import Model.Restaurant.Restaurant;
import Model.Feedback.Feedback;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

@Getter
@Setter
@Service
public class MizDooni {

    private int reservationNumber = 1;
    private ArrayList<User> users = new ArrayList<>();
    private ArrayList<Restaurant> restaurants= new ArrayList<>();
    private ArrayList<Feedback> feedbacks = new ArrayList<>();
    private ArrayList<Table> tables = new ArrayList<>();
    private static MizDooni instance;
    private User loggedInUser = null;
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final String USERS_FILE_PATH = "json/users.json";
    private static final String RESTAURANTS_FILE_PATH = "json/restaurants.json";
    private static final String FEEDBACKS_FILE_PATH = "json/feedbacks.json";
    public static MizDooni getInstance() {
        if (instance == null) {
            instance = new MizDooni();
        }
        return instance;
    }

    public void fetchAndStoreDataFromAPI() throws IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();

        // Define the endpoint URLs
        String restaurantsEndpoint = "http://91.107.137.117:55/restaurants";
        String reviewsEndpoint = "http://91.107.137.117:55/reviews";
        String usersEndpoint = "http://91.107.137.117:55/users";
        String tablesEndpoint = "http://91.107.137.117:55/tables";

        // Fetch restaurants
        CloseableHttpResponse restaurantsResponse = httpClient.execute(new HttpGet(restaurantsEndpoint));
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            List<Restaurant> fetchedRestaurants = objectMapper.readValue(restaurantsResponse.getEntity().getContent(), new TypeReference<List<Restaurant>>() {
            });
            restaurants.addAll(fetchedRestaurants);
        } finally {
            restaurantsResponse.close();
        }
        // Generate unique IDs for restaurants
        for (Restaurant restaurant : restaurants) {
            int uniqueId = generateUniqueId(restaurant);
            restaurant.setId(uniqueId);
        }

        // Fetch users
        CloseableHttpResponse usersResponse = httpClient.execute(new HttpGet(usersEndpoint));
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            List<User> fetchedUsers = objectMapper.readValue(usersResponse.getEntity().getContent(), new TypeReference<List<User>>() {
            });
            users.addAll(fetchedUsers);
        } finally {
            usersResponse.close();
        }
        // Fetch reviews
        CloseableHttpResponse reviewsResponse = httpClient.execute(new HttpGet(reviewsEndpoint));
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            List<Feedback> fetchedFeedbacks = objectMapper.readValue(reviewsResponse.getEntity().getContent(), new TypeReference<List<Feedback>>() {
            });

            // Match each review with the corresponding restaurant
            for (Feedback feedback : fetchedFeedbacks) {
                for (Restaurant restaurant : restaurants) {
                    if (restaurant.getName().equals(feedback.getRestaurantName())) {
                        restaurant.getFeedbacks().add(feedback);
                        // Calculate and update averages
                        updateAverages(restaurant, feedback);
                        break; // Assuming restaurant names are unique, no need to continue searching
                    }
                }
            }

            // Update users with their feedbacks
            for (User user : users) {
                for (Feedback feedback : fetchedFeedbacks) {
                    if (user.getUsername().equals(feedback.getUsername())) {
                        user.getFeedbacks().add(feedback);
                        break; // Assuming usernames are unique, no need to continue searching
                    }
                }
            }
        } finally {
            reviewsResponse.close();
        }


        //ADD A RESERVATION.
        User user = getUserByUsername("Mostafa_Ebrahimi");
        Restaurant restaurant = getRestaurantByName("The Commoner");

        LocalDate date = LocalDate.now().minusDays(2); // Assuming the reservation is for tomorrow
        LocalTime time = LocalTime.now();

        // Set the minutes to zero
        LocalTime timeWithZeroMinutes = time.withMinute(0);

        // Reassign the modified LocalTime back to the original variable
        time = timeWithZeroMinutes;
        System.out.println(date);
        System.out.println(time);
        Reservation reservation = new Reservation(user.getUsername(), restaurant.getName(), 1, generateReservationNumber(), date, time, 388006555, 4);
        user.addReservation(reservation);
        restaurant.addReservation(reservation);
        users.set(6, user);
        restaurants.set(0, restaurant);
        // Check if reservation was added successfully
        if (reservation != null) {
            System.out.println("Reservation Number: " + reservation.getReservationNumber());
            System.out.println("Reservation Date and Time: " + reservation.getDate());
        }

        date = LocalDate.now().plusDays(1);// Assuming the reservation is for tomorrow
        time = LocalTime.now(); // Assuming the reservation is for tomorrow
        timeWithZeroMinutes = time.withMinute(0);
        time = timeWithZeroMinutes;
        System.out.println(date);
        reservation = new Reservation(user.getUsername(), restaurant.getName(), 1, generateReservationNumber(), date, time, 388006555, 4);
        user.addReservation(reservation);
        restaurant.addReservation(reservation);
        users.set(6, user);
        restaurants.set(0, restaurant);
// Fetch tables
        CloseableHttpResponse tablesResponse = httpClient.execute(new HttpGet(tablesEndpoint));
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            List<Table> fetchedTables = objectMapper.readValue(tablesResponse.getEntity().getContent(), new TypeReference<List<Table>>() {});

            // Match each table with the corresponding restaurant
            for (Restaurant rest : restaurants) {
                for (Table table : fetchedTables) {
                    if (rest.getName().equals(table.getRestaurantName())) {
                        // Convert restaurant start time from string to integer representing the hour
                        int openingHour = Integer.parseInt(rest.getStartTime().split(":")[0]);
                        table.setOpeningTime(openingHour);
                        // Convert restaurant end time from string to integer representing the hour
                        int closingHour = Integer.parseInt(rest.getEndTime().split(":")[0]);
                        table.setClosingTime(closingHour);
                        table.makeTimeSlots();
                        // Update reservation status for each table based on restaurant reservations
                        for (Reservation  reserv: rest.getReservations()) {
                            if (reserv.getTableNumber() == table.getTableNumber()) {
                                // Update reservation status for the corresponding time slot
                                table.getReservations().add(reserv);
                            }
                        }

                        rest.getTables().add(table);
                        System.out.println(table.getRestaurantName());
                        System.out.println(table.getTableNumber());
                        System.out.println(rest.getName());

                        tables.add(table);

                    }
                }
                System.out.println(rest.getTables());
            }
        } finally {
            tablesResponse.close();
        }



        httpClient.close();
    }
private int generateReservationNumber() {
    return (int) (Math.random() * 1000000);
}
    private static int generateUniqueId(Restaurant restaurant) {
        // Concatenate restaurant attributes to generate a unique string
        String uniqueString = restaurant.getName() + restaurant.getManagerUsername()+ restaurant.getAddress().getCity() + restaurant.getAddress().getCountry();

        // Generate unique ID
        return Math.abs(uniqueString.hashCode());
    }

    private Restaurant updateAverages(Restaurant restaurant, Feedback feedback) {
        // Incrementally update the averages
        int numFeedbacks = restaurant.getFeedbacks().size(); // Number of existing feedbacks

        // Calculate the new averages based on the existing averages and the new feedback
        double newServiceAvg = ((restaurant.getServiceAvg() * numFeedbacks) + feedback.getServiceRate()) / (numFeedbacks + 1);
        double newFoodAvg = ((restaurant.getFoodAvg() * numFeedbacks) + feedback.getFoodRate()) / (numFeedbacks + 1);
        double newAmbianceAvg = ((restaurant.getAmbianceAvg() * numFeedbacks) + feedback.getAmbianceRate()) / (numFeedbacks + 1);
        double newOverallAvg = ((restaurant.getOverallAvg() * numFeedbacks) + feedback.getOverallRate()) / (numFeedbacks + 1);

        // Update the averages in the restaurant object
        restaurant.setServiceAvg(newServiceAvg);
        restaurant.setFoodAvg(newFoodAvg);
        restaurant.setAmbianceAvg(newAmbianceAvg);
        restaurant.setOverallAvg(newOverallAvg);
        return restaurant;
    }

//    public void fetchAndStoreDataFromAPI() throws IOException {
//        CloseableHttpClient httpClient = HttpClients.createDefault();
//        System.out.println("Hello, world");
//        // Define the endpoint URLs
//        String restaurantsEndpoint = "http://91.107.137.117:55/restaurants";
//        String reviewsEndpoint = "http://91.107.137.117:55/reviews";
//        String usersEndpoint = "http://91.107.137.117:55/users";
//        String tablesEndpoint = "http://91.107.137.117:55/tables";
//
//        CloseableHttpResponse restaurantsResponse = httpClient.execute(new HttpGet(restaurantsEndpoint));
//        try {
//            ObjectMapper objectMapper = new ObjectMapper();
//            List<Restaurant> fetchedRestaurants = objectMapper.readValue(restaurantsResponse.getEntity().getContent(), new TypeReference<List<Restaurant>>() {});
//            restaurants.addAll(fetchedRestaurants);
//        } finally {
//            restaurantsResponse.close();
//        }
//
//        CloseableHttpResponse reviewsResponse = httpClient.execute(new HttpGet(reviewsEndpoint));
//        try {
//            ObjectMapper objectMapper = new ObjectMapper();
//            List<Feedback> fetchedFeedbacks = objectMapper.readValue(reviewsResponse.getEntity().getContent(), new TypeReference<List<Feedback>>() {});
//            feedbacks.addAll(fetchedFeedbacks);
//        } finally {
//            reviewsResponse.close();
//        }
//
//        CloseableHttpResponse usersResponse = httpClient.execute(new HttpGet(usersEndpoint));
//
//        try {
//            ObjectMapper objectMapper = new ObjectMapper();
//            List<User> fetchedUsers = objectMapper.readValue(usersResponse.getEntity().getContent(), new TypeReference<List<User>>() {});
//            users.addAll(fetchedUsers);
//        } finally {
//            usersResponse.close();
//        }
//
//        CloseableHttpResponse tablesResponse = httpClient.execute(new HttpGet(tablesEndpoint));
//        try {
//            ObjectMapper objectMapper = new ObjectMapper();
//            List<Table> fetchedTables = objectMapper.readValue(tablesResponse.getEntity().getContent(), new TypeReference<List<Table>>() {});
//            tables.addAll(fetchedTables);
//        } finally {
//            tablesResponse.close();
//        }
//
//        httpClient.close();
//
//    }
    public void loadUsersFromJson() {
        try {
            users = new ArrayList<>(loadFromJsonFile(USERS_FILE_PATH, new TypeReference<List<User>>() {}));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadRestaurantsFromJson() {
        try {
            restaurants = new ArrayList<>(loadFromJsonFile(RESTAURANTS_FILE_PATH, new TypeReference<List<Restaurant>>() {}));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public <T> List<T> loadFromJsonFile(String filePath, TypeReference<List<T>> typeReference) throws IOException {
        try (InputStream inputStream = MizDooni.class.getClassLoader().getResourceAsStream(filePath)) {
            if (inputStream == null) {
                throw new IOException("File not found: " + filePath);
            }
            return objectMapper.readValue(inputStream, typeReference);
        }
    }

    public void addUser(User user) throws Exception{
        users.add(user);
    }

    public void addRestaurant(Restaurant restaurant){
        restaurants.add(restaurant);
    }
    public boolean isUserExists(String username) {
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                return true;
            }
        }
        return false; // Username not found
    }

    public void addTable(Restaurant restaurant, Table table){
        restaurant.addTable(table);
    }
    public boolean isEmailExists(String email) {
        for (User user : users) {
            if (user.getEmail().equals(email)) {
                return true;
            }
        }
        return false; // Email not found
    }

    public boolean isRestaurantNameExists(String name){
        for(Restaurant restaurant : restaurants){
            if(restaurant.getName().equals(name)){
                return true;
            }
        }
        return false;
    }

    public boolean isRestaurantNameAvailable(String name){
        for(Restaurant restaurant : restaurants){
            if(restaurant.getName().contains(name)){
                return true;
            }
        }
        return false;
    }

    public boolean isManager(String username){
        for(User user : users){
            if(user.getUsername().equals(username)){
                return user.getRole().equals("manager");
            }
        }
        return false;
    }


    public User getUserByUsername(String username){
        User res = null;
        for(User user : users){
            if(user.getUsername().equals(username))
                res =  user;
        }
        return res;
    }

    public List<Reservation> getUserHistory(String username){
        User user = getUserByUsername(username);
        return user.getReservations();
    }


    public void addFeedback(Feedback feedback){

        this.feedbacks.add(feedback);
    }

    public Restaurant getRestaurantByName(String restaurantName) {
        for (Restaurant restaurant : restaurants) {
            if (restaurant.getName().equals(restaurantName)) {
                return restaurant;
            }
        }
        return null; // Restaurant not found
    }
    public Feedback getFeedbackByName(String username, String restaurantName){
        for(Feedback feedback: feedbacks){
            if(feedback.getRestaurantName()== restaurantName && feedback.getUsername()==username){
                return feedback;
            }
        }
        return null;
    }

    public void updateFeedback(Feedback feedback){
        this.removeFeedback(feedback.getUsername(),feedback.getRestaurantName());
        this.feedbacks.add(feedback);
    }

    private void removeFeedback(String username, String restaurantName){
        int restaurant_index, user_index;
        restaurant_index = -1;
        user_index = -1;
        int restaurant_counter =0;
        int user_counter = 0;
        int restaurant_feedback_index = -1;
        int user_feedback_index = -1;
        int feedbackCounter = 0;
        for(Feedback feedback: feedbacks){
            for (Restaurant restaurant: restaurants){
              if(restaurant.getName().equals(feedback.getRestaurantName())){
                  restaurant_index = restaurant_counter;
                  break;
              }
                restaurant_counter = restaurant_counter+ 1;
            }
            for(User user: users){
                if(user.getUsername().equals(feedback.getUsername())){
                    user_index = user_counter;
                    break;
                }
                user_counter = user_counter + 1;
            }
            feedbackCounter = feedbackCounter + 1;
        }
        if(restaurant_counter != -1){
            for(int i =0; i < feedbacks.size();i++){
                if(feedbacks.get(i).getRestaurantName()== restaurantName && feedbacks.get(i).getUsername()==username){
                    feedbacks.remove(i);
//                    TODO
                    restaurants.get(restaurant_index).getFeedbacks().remove(i);
                    break;
                }
            }
        }

    }

    public boolean isLoggedIn(){
        return loggedInUser != null;
    }

    public User doesAccountExists(String username, String password){
        for(User user: users){
            if (user.getUsername().equals(username) && user.getPassword().equals(password)){
                return user;
            }
        }
        return null;
    }

//    public String login(User user){
//        this.loggedInUser = user;
//        String URL ;
//        if(isManager(user.getUsername())){
//            URL = "manager_home.jsp";
//        }
//        else{
//            URL = "client_home.jsp";
//        }
//        return URL;
//    }

    public boolean doesUserHaveReserve(String username, String restaurantName){
        for(Restaurant restaurant: restaurants){
            if(restaurant.getName().equals(restaurantName) && restaurant.doesReserveExists(username)){
                return true;
            }
        }
        return false;
    }

    public boolean isFeedbackTimeCorrect(String username , String restaurantName){
        for(Restaurant restaurant : restaurants){
            if(restaurant.getName().equals(restaurantName) && restaurant.doesReserveExists(username) && restaurant.isTimeOk(username)){
                return true;
            }
        }

        return false;
    }

    public String createFeedbackHTML(String username , String restaurantName){
        String html = "";
        for (Feedback feedback: feedbacks){
            if (feedback.getUsername().equals(username) && feedback.getRestaurantName().equals(restaurantName)){
                html += feedback.toHtml();
            }
        }

        return html;
    }

    public void updateRestaurantRatings(String restaurantName, double foodRate,
                                        double serviceRate, double ambianceRate,double overallRate){
        Restaurant restaurant = getRestaurantByName(restaurantName);
        int sizeOfFeedbacks = restaurant.getFeedbacks().size()-1;
        System.out.println(restaurant.getFoodAvg());
        System.out.println(sizeOfFeedbacks);
        System.out.println(foodRate);
        double foodAvg = (foodRate + (restaurant.getFoodAvg()*(sizeOfFeedbacks)))/(sizeOfFeedbacks+1);
        foodAvg = Math.round(foodAvg * 100) / 100.0;
        double serviceAvg = (serviceRate + (((restaurant.getServiceAvg() * (sizeOfFeedbacks))) ))/ (sizeOfFeedbacks + 1);
        serviceAvg = Math.round(serviceAvg * 100) / 100.0;
        double ambianceAvg = Math.round(((ambianceRate + ((restaurant.getAmbianceAvg()*(sizeOfFeedbacks))))/(sizeOfFeedbacks+1))*100)/100.0;
        double overallAvg = Math.round(((overallRate + (restaurant.getOverallAvg()*(sizeOfFeedbacks)))/(sizeOfFeedbacks+1))*100)/100.0;
        restaurant.updateRatingsAvg(foodAvg,serviceAvg,ambianceAvg,overallAvg);
    }

    public String createManagerRestaurantHtml(String username){
        String html = "";
        for (int i=0;i<restaurants.size();i++){
            if (restaurants.get(i).getManagerUsername().equals(username)){
                html += restaurants.get(i).toHtml(i+1);
                break;
            }
        }

        return html;
    }

    public void logout(){
        this.loggedInUser = null;
    }

    public User findUserByUsername(String username) throws SuperException {
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        throw new SuperException(ExceptionMessages.USER_NOT_FOUND + username);
    }
    public List<Restaurant> getRestaurantsByName(String name){
        List<Restaurant> res = new ArrayList<>();
        for(Restaurant restaurant : restaurants){
            if (restaurant.getName().contains(name)){
                res.add(restaurant);
            }
        }
        return res;
    }

    public List<Restaurant> getRestaurantsByType(String type){
        List<Restaurant> res = new ArrayList<>();
        for(Restaurant restaurant : restaurants){
            if (restaurant.getType().equals(type)){
                res.add(restaurant);
            }
        }
        return res;
    }

    public List<Restaurant> searchRestaurantsByName(String name) throws SuperException {
        List<Restaurant> restaurantsByName = getRestaurantsByName(name);
        if (restaurantsByName.isEmpty()) {
            throw new SuperException (ExceptionMessages.NO_RESTAURANT_WITH_NAME + name);
        }
        return restaurantsByName;
    }

    public List<Restaurant> searchRestaurantsByType(String type) throws SuperException {
        List<Restaurant> restaurantsByType = getRestaurantsByType(type);
        if (restaurantsByType.isEmpty()) {
            throw new SuperException(ExceptionMessages.NO_RESTAURANT_WITH_TYPE + type);
        }
        return restaurantsByType;
    }

    public List<Restaurant> searchRestaurantsByCity(String city) throws SuperException {
        List<Restaurant> restaurantsByCity = getRestaurantsByCity(city);
        if (restaurantsByCity.isEmpty()) {
            throw new SuperException(ExceptionMessages.NO_RESTAURANT_WITH_CITY + city);
        }
        return restaurantsByCity;
    }

    public List<Restaurant> getRestaurantsByCity(String city){
        List<Restaurant> res = new ArrayList<>();
        for(Restaurant restaurant : restaurants){
            if (restaurant.getAddress().getCity().equals(city)){
                res.add(restaurant);
            }
        }
        return res;
    }
    public List<Restaurant> getRestaurants() {
        return restaurants;
    }

    public List<Restaurant> sortRestaurantsByScore() {
        // Sort the list of restaurants based on overall score
        Collections.sort(restaurants, new Comparator<Restaurant>() {
            @Override
            public int compare(Restaurant restaurant1, Restaurant restaurant2) {
                // Compare based on overall score
                double overallScore1 = (restaurant1.getServiceAvg() + restaurant1.getFoodAvg() + restaurant1.getAmbianceAvg()) / 3.0;
                double overallScore2 = (restaurant2.getServiceAvg() + restaurant2.getFoodAvg() + restaurant2.getAmbianceAvg()) / 3.0;
                return Double.compare(overallScore2, overallScore1); // Descending order
            }
        });

        return restaurants;
    }

    public List<String> getAllRestaurantTypes() {
        List<Restaurant> allRestaurants = restaurants;
        List<String> restaurantTypes = new ArrayList<>();
        for (Restaurant restaurant : allRestaurants) {
            String type = restaurant.getType();
            if (!restaurantTypes.contains(type)) {
                restaurantTypes.add(type);
            }
        }
        Collections.sort(restaurantTypes);
        return restaurantTypes;
    }

    public Map<String, List<String>> getCountriesAndCities() {
        List<Restaurant> allRestaurants = restaurants;

        Map<String, List<String>> countryCityMap = new HashMap<>();

        for (Restaurant restaurant : allRestaurants) {
            String country = restaurant.getAddress().getCountry();
            String city = restaurant.getAddress().getCity();

            countryCityMap.putIfAbsent(country, new ArrayList<>());
            if(!countryCityMap.get(country).contains(city)){
                countryCityMap.get(country).add(city);
            }
        }

        return countryCityMap;
    }
    public Map<String, Map<String, List<String>>> getTypesCountriesAndCities() {
        List<Restaurant> allRestaurants = restaurants;

        Map<String, Map<String, List<String>>> typeCountryCityMap = new HashMap<>();

        for (Restaurant restaurant : allRestaurants) {
            String type = restaurant.getType();
            String country = restaurant.getAddress().getCountry();
            String city = restaurant.getAddress().getCity();

            typeCountryCityMap.putIfAbsent(type, new HashMap<>());

            typeCountryCityMap.get(type).putIfAbsent(country, new ArrayList<>());

            typeCountryCityMap.get(type).get(country).add(city);
        }

        return typeCountryCityMap;
    }
    public User login(String username, String password) throws SuperException {
        User user = findUserByUsername(username);

        if (user != null && user.getPassword().equals(password)) {
            loggedInUser = user;
            return user;
        } else {
            throw new SuperException(ExceptionMessages.INVALID_USERNAME_PASSWORD);
        }
    }
    public User signUp(String username, String password, String email, String role, String city, String country) throws SuperException {
//        if (findUserByUsername(username) != null) {
//            throw new SuperException(ExceptionMessages.USERNAME_ALREADY_EXISTS_EXCEPTION_MESSAGE);
//        }
        User u = null;
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                u =  user;
            }
        }

        if (u != null){
            throw new SuperException(ExceptionMessages.USERNAME_ALREADY_EXISTS_EXCEPTION_MESSAGE);
        }
        AddressUser addressUser = new AddressUser();
        addressUser.setCity(city);
        addressUser.setCountry(country);
        User newUser = new User(username, email, password, role, addressUser);
        users.add(newUser);
        return newUser;

    }

    public boolean isReservationTimePassed(User user, Restaurant restaurant) {
        // Assuming you have a method to get the user's reservations
        List<Reservation> reservations = getUserHistory(user.getUsername());

        // Check if there's any reservation for the restaurant
        for (Reservation reservation : reservations) {
            if (reservation.getRestaurantName().equals(restaurant.getName()) && reservation.getDate().isBefore(LocalDate.now())) {
                return true; // Reservation time has passed
            }
        }
        return false; // No reservation or reservation time not passed
    }


    public Restaurant getRestaurantById ( int id ) {
        Restaurant finalRestaurant = new Restaurant();
        for(Restaurant restaurant : restaurants){
            if(restaurant.getId() == id){
                finalRestaurant = restaurant;
                break;
            }
        }
        return finalRestaurant;
    }

    public void addReservation( String username,String restaurantName,int tableNumber,String date,String time){
        System.out.println("1");
        Restaurant restaurant = getRestaurantByName(restaurantName);
        System.out.println("2");
        User user = getUserByUsername(username);
        System.out.println("3");
        LocalDate lDate = LocalDate.parse(date);
        System.out.println("4");
        LocalTime lTime = LocalTime.parse(time+":00");
        System.out.println("5");
        Table table = restaurant.getTableByNumber(tableNumber);
        System.out.println("6");
        Reservation reservation = new Reservation(user.getUsername(), restaurant.getName(), tableNumber, generateReservationNumber(), lDate, lTime,restaurant.getId(), table.getSeatsNumber());
        System.out.println("7");
        user.addReservation(reservation);
        System.out.println("8");
        restaurant.addReservation(reservation);
        System.out.println("9");
    }
    public void updateUsers(User user){
        int i = 0;
        for (User tmp: users){
            if(tmp.getUsername().equals(user.getUsername())){
                users.set(i, user);
                break;
            }
            i = i + 1;
        }
    }
    public void updateRestaurants(Restaurant restaurant){
        int i = 0;
        for (Restaurant tmp: restaurants){
            if(tmp.getName().equals(restaurant.getName())){
                restaurants.set(i, restaurant);
                break;
            }
            i = i + 1;
        }
    }
    public void retractReview(Feedback existingReview, double newFoodRate, double newServiceRate, double newAmbianceRate, double newOverallRate, String newComment) {
//        for(int i = 0; i < users.size()-1)
        System.out.println('k');
        for (User user : users) {
            if (user.getUsername().equals(existingReview.getUsername())) {
                List<Feedback> userFeedbacks = user.getFeedbacks();
                Iterator<Feedback> iterator = userFeedbacks.iterator();
                while (iterator.hasNext()) {
                    Feedback feedback = iterator.next();
                    if (feedback.equals(existingReview)) {
                        // Remove the existing review's comment from the user's feedbacks
                        iterator.remove();
                        break; // Assuming review IDs are unique, no need to continue searching
                    }
                }
                System.out.println(user.getFeedbacks());
                // Update the user's feedbacks list
                user.setFeedbacks(userFeedbacks);
                System.out.println(user.getFeedbacks());

                break; // Assuming usernames are unique, no need to continue searching
            }
        }

        for (Restaurant restaurant : restaurants) {
            if (restaurant.getName().equals(existingReview.getRestaurantName())) {
                List<Feedback> userFeedbacks = restaurant.getFeedbacks();
                Iterator<Feedback> iterator = userFeedbacks.iterator();
                while (iterator.hasNext()) {
                    Feedback feedback = iterator.next();
                    if (feedback.equals(existingReview)) {
                        // Remove the existing review's comment from the user's feedbacks
                        iterator.remove();
                        break; // Assuming review IDs are unique, no need to continue searching
                    }
                }
                System.out.println(restaurant.getFeedbacks());
                // Update the user's feedbacks list
                restaurant.setFeedbacks(userFeedbacks);
                System.out.println(restaurant.getFeedbacks());

                break; // Assuming usernames are unique, no need to continue searching
            }
        }

        // Retrieve the user and restaurant associated with the review
//        User user = getUserByUsername(existingReview.getUsername());
//        Restaurant restaurant = getRestaurantByName(existingReview.getRestaurantName());
//
//        // Find the existing review
//        Feedback previousReview = null;
//        for (Feedback review : user.getFeedbacks()) {
//            if (review.equals(existingReview)) {
//                previousReview = review;
//                break;
//            }
//        }
//
//        if (previousReview == null) {
//            // The existing review was not found
//            return;
//        }
//
//        // Retrieve the previous ratings and comment
//        double previousFoodRate = previousReview.getFoodRate();
//        double previousServiceRate = previousReview.getServiceRate();
//        double previousAmbianceRate = previousReview.getAmbianceRate();
//        double previousOverallRate = previousReview.getOverallRate();
////        String previousComment = previousReview.getComment();
//
//        // Calculate the mean ratings without considering the existing review
//        int totalReviews = restaurant.getFeedbacks().size(); // Exclude the existing review
//        double meanFoodRate = (restaurant.getFoodAvg() * totalReviews - previousFoodRate + newFoodRate) / totalReviews;
//        double meanServiceRate = (restaurant.getServiceAvg() * totalReviews - previousServiceRate + newServiceRate) / totalReviews;
//        double meanAmbianceRate = (restaurant.getAmbianceAvg() * totalReviews - previousAmbianceRate + newAmbianceRate) / totalReviews;
//        double meanOverallRate = (restaurant.getOverallAvg() * totalReviews - previousOverallRate + newOverallRate) / totalReviews;
//
//        // Update the restaurant ratings
//        restaurant.setFoodAvg(meanFoodRate);
//        restaurant.setServiceAvg(meanServiceRate);
//        restaurant.setAmbianceAvg(meanAmbianceRate);
//        restaurant.setOverallAvg(meanOverallRate);
//
//        // Update the existing review with the new comment and adjusted ratings
//        previousReview.setFoodRate(newFoodRate);
//        previousReview.setServiceRate(newServiceRate);
//        previousReview.setAmbianceRate(newAmbianceRate);
//        previousReview.setOverallRate(newOverallRate);
//        previousReview.setComment(newComment);
//
//
//        int index = -1;
//        for(int i = 0; i < users.size()-1; i++){
//            if(user.getFeedbacks().get(i).getRestaurantName().equals(restaurant.getName())){
//                index = i;
//                break;
//            }
//        }
//        // Update the user and restaurant in the database
//        updateUsers(user);
//        updateRestaurants(restaurant);
    }

}
