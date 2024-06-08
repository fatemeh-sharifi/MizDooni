package Controller.Restaurant;

import Entity.Feedback.FeedbackEntity;
import Entity.Restaurant.RestaurantEntity;
import Model.Restaurant.Restaurant;
import Repository.Address.AddressRestaurantRepository;
import Repository.Feedback.FeedbackRepository;
import Repository.Restaurant.RestaurantRepository;
import Repository.User.ClientRepository;
import Service.Restaurant.RestaurantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import co.elastic.apm.api.ElasticApm;
import co.elastic.apm.api.Scope;
import co.elastic.apm.api.Span;
import co.elastic.apm.api.Transaction;
@RestController
public class RestaurantController {


    @Autowired
    RestaurantRepository restaurantRepository;
    @Autowired
    FeedbackRepository feedbackRepository;
    @Autowired
    ClientRepository clientRepository;
    @Autowired
    RestaurantService restaurantService;
    @Autowired
    private AddressRestaurantRepository addressRestaurantRepository;

//    @GetMapping ( "/restaurants" )
//    public ResponseEntity < List < Restaurant > > findRestaurants (
//            @RequestParam ( required = false ) String type ,
//            @RequestParam ( required = false ) String city ,
//            @RequestParam ( required = false ) String country ,
//            @RequestParam ( required = false ) String name
//    ) {
//        try {
//            List < RestaurantEntity > filteredRestaurants = restaurantRepository.findRestaurants ( type , city , country , name );
//            List < Restaurant > restaurants = restaurantService.getAllRestaurantsWithFeedbacks ( filteredRestaurants );
//            return ResponseEntity.ok ( ).body ( restaurants );
//        } catch ( Exception e ) {
//            return ResponseEntity.badRequest ( ).body ( null );
//        }
//    }
//
//    @GetMapping ( "/restaurants/{id}" )
//    public ResponseEntity < Restaurant > findRestaurantById ( @PathVariable int id ) {
//        try {
//            RestaurantEntity restaurant = restaurantRepository.findRestaurantById ( id );
//            if ( restaurant != null ) {
//                List < RestaurantEntity > entity = new ArrayList <> ( );
//                entity.add ( restaurant );
//                List < Restaurant > model = restaurantService.getAllRestaurantsWithFeedbacks ( entity );
//                Restaurant response = model.get ( 0 );
//                return ResponseEntity.ok ( ).body ( response );
//            } else {
//                return ResponseEntity.notFound ( ).build ( );
//            }
//        } catch ( Exception e ) {
//            return ResponseEntity.badRequest ( ).body ( null );
//        }
//    }
//
//    @GetMapping ( "/topRestaurants" )
//    public ResponseEntity < List < Restaurant > > findTopRestaurants (
//            @RequestParam ( required = false ) String username ,
//            @RequestParam ( required = false ) String type ,
//            @RequestParam ( required = false ) String city ,
//            @RequestParam ( required = false ) String country ) {
//        try {
//            List < RestaurantEntity > topRestaurants = restaurantRepository.findTopRestaurants ( username , type , city , country );
//            List < Restaurant > topRestaurantsModel = restaurantService.getAllRestaurantsWithFeedbacks ( topRestaurants );
//            List < Restaurant > top6RestaurantsModel = topRestaurantsModel.stream ( ).limit ( 6 ).collect ( Collectors.toList ( ) );
//            return ResponseEntity.ok ( ).body ( top6RestaurantsModel );
//        } catch ( Exception e ) {
//            return ResponseEntity.badRequest ( ).body ( null );
//        }
//    }
//
//    @GetMapping ( "/types" )
//    public ResponseEntity < List < String > > findRestaurantsTypes ( ) {
//        try {
//            List < String > types = restaurantRepository.findDistinctTypes ( );
//            return ResponseEntity.ok ( ).body ( types );
//        } catch ( Exception e ) {
//            return ResponseEntity.badRequest ( ).body ( null );
//        }
//    }
//
//    @GetMapping ( "/location" )
//    public ResponseEntity < Map < String, List < String > > > findRestaurantsLocation ( ) {
//        try {
//            Map < String, List < String > > countryCityMap = restaurantService.getCountriesAndCities ( );
//            return ResponseEntity.ok ( ).body ( countryCityMap );
//        } catch ( Exception e ) {
//            return ResponseEntity.badRequest ( ).body ( null );
//        }
//
//    }
//
//    @GetMapping ( "/typesCountriesAndCities" )
//    public ResponseEntity < Map < String, Map < String, List < String > > > > findTypesCountriesAndCities ( ) {
//        try {
//            Map < String, Map < String, List < String > > > typeCountryCityMap = restaurantService.getTypesCountriesAndCities ( );
//            return ResponseEntity.ok ( ).body ( typeCountryCityMap );
//        } catch ( Exception e ) {
//            return ResponseEntity.badRequest ( ).body ( null );
//        }
//    }
//
//    @PostMapping ( "/reviews" )
//    public ResponseEntity < String > addOrUpdateReview (
//            @RequestParam String username ,
//            @RequestParam String restaurantName ,
//            @RequestParam ( required = false ) Double foodRate ,
//            @RequestParam ( required = false ) Double serviceRate ,
//            @RequestParam ( required = false ) Double ambianceRate ,
//            @RequestParam ( required = false ) Double overallRate ,
//            @RequestParam ( required = false ) String comment
//    ) {
//        try {
//            // Check if any review parameter is provided
//            if ( foodRate == null && serviceRate == null && ambianceRate == null && overallRate == null && comment == null ) {
//                return ResponseEntity.ok ( "Review parameters are valid, but review update not requested" );
//            }
//            System.out.println ( feedbackRepository.findAll ( ).size ( ) );
//            // Update feedback
//
//            FeedbackEntity feedbackEntity = feedbackRepository.findByCustomerUsernameAndRestaurantName ( username , restaurantName );
//            if ( feedbackEntity == null ) {
//                LocalDateTime localDateTime = LocalDateTime.now ( );
//                FeedbackEntity newFeedbackEntity = new FeedbackEntity ( clientRepository.findByUsername ( username ) , restaurantRepository.findByName ( restaurantName ) , foodRate , serviceRate , ambianceRate , overallRate , comment , localDateTime );
//                feedbackRepository.save ( newFeedbackEntity );
//                restaurantService.updateRestaurantRatings ( restaurantName , foodRate , serviceRate , ambianceRate , overallRate );
//            } else {
//                if ( foodRate != null && serviceRate != null && ambianceRate != null && overallRate != null && comment != null ) {
//                    feedbackRepository.updateExistingFeedback ( username , restaurantName , foodRate , serviceRate , ambianceRate , overallRate , comment );
//                    restaurantService.updateRestaurantRatings ( restaurantName , foodRate , serviceRate , ambianceRate , overallRate );
//                }
//            }
//            return ResponseEntity.ok ( "Review added/updated successfully" );
//        } catch ( Exception e ) {
//            return ResponseEntity.status ( 400 ).body ( "Failed to add/update review: " + e.getMessage ( ) );
//        }
//
//    }
    @GetMapping("/restaurants")
    public ResponseEntity<List<Restaurant>> findRestaurants(
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String country,
            @RequestParam(required = false) String name) {
        Transaction transaction = ElasticApm.startTransaction();
        try (Scope txScope = transaction.activate()) {
            transaction.setName("HTTP GET /restaurants");
            transaction.setType(Transaction.TYPE_REQUEST);

            Span dbSpan = transaction.startSpan("db", "query", "findRestaurants");
            try (Scope dbScope = dbSpan.activate()) {
                dbSpan.setName("Find Restaurants");

                List<RestaurantEntity> filteredRestaurants = restaurantRepository.findRestaurants(type, city, country, name);
                dbSpan.end();

                Span businessLogicSpan = transaction.startSpan("custom", "logic", "getAllRestaurantsWithFeedbacks");
                try (Scope businessLogicScope = businessLogicSpan.activate()) {
                    businessLogicSpan.setName("Get All Restaurants With Feedbacks");

                    List<Restaurant> restaurants = restaurantService.getAllRestaurantsWithFeedbacks(filteredRestaurants);
                    businessLogicSpan.end();

                    return ResponseEntity.ok().body(restaurants);
                } catch (Exception e) {
                    businessLogicSpan.captureException(e);
                    businessLogicSpan.end();
                    throw e;
                }
            } catch (Exception e) {
                dbSpan.captureException(e);
                dbSpan.end();
                throw e;
            }
        } catch (Exception e) {
            transaction.captureException(e);
            return ResponseEntity.badRequest().body(null);
        } finally {
            transaction.end();
        }
    }

    @GetMapping("/restaurants/{id}")
    public ResponseEntity<Restaurant> findRestaurantById(@PathVariable int id) {
        Transaction transaction = ElasticApm.startTransaction();
        try (Scope txScope = transaction.activate()) {
            transaction.setName("HTTP GET /restaurants/{id}");
            transaction.setType(Transaction.TYPE_REQUEST);

            Span dbSpan = transaction.startSpan("db", "query", "findRestaurantById");
            try (Scope dbScope = dbSpan.activate()) {
                dbSpan.setName("Find Restaurant By ID");

                RestaurantEntity restaurant = restaurantRepository.findRestaurantById(id);
                dbSpan.end();

                if (restaurant != null) {
                    List<RestaurantEntity> entity = new ArrayList<>();
                    entity.add(restaurant);

                    Span businessLogicSpan = transaction.startSpan("custom", "logic", "getAllRestaurantsWithFeedbacks");
                    try (Scope businessLogicScope = businessLogicSpan.activate()) {
                        businessLogicSpan.setName("Get All Restaurants With Feedbacks");

                        List<Restaurant> model = restaurantService.getAllRestaurantsWithFeedbacks(entity);
                        businessLogicSpan.end();

                        Restaurant response = model.get(0);
                        return ResponseEntity.ok().body(response);
                    } catch (Exception e) {
                        businessLogicSpan.captureException(e);
                        businessLogicSpan.end();
                        throw e;
                    }
                } else {
                    return ResponseEntity.notFound().build();
                }
            } catch (Exception e) {
                dbSpan.captureException(e);
                dbSpan.end();
                throw e;
            }
        } catch (Exception e) {
            transaction.captureException(e);
            return ResponseEntity.badRequest().body(null);
        } finally {
            transaction.end();
        }
    }

    @GetMapping("/topRestaurants")
    public ResponseEntity<List<Restaurant>> findTopRestaurants(
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String country) {
        Transaction transaction = ElasticApm.startTransaction();
        try (Scope txScope = transaction.activate()) {
            transaction.setName("HTTP GET /topRestaurants");
            transaction.setType(Transaction.TYPE_REQUEST);

            Span dbSpan = transaction.startSpan("db", "query", "findTopRestaurants");
            try (Scope dbScope = dbSpan.activate()) {
                dbSpan.setName("Find Top Restaurants");

                List<RestaurantEntity> topRestaurants = restaurantRepository.findTopRestaurants(username, type, city, country);
                dbSpan.end();

                Span businessLogicSpan = transaction.startSpan("custom", "logic", "getAllRestaurantsWithFeedbacks");
                try (Scope businessLogicScope = businessLogicSpan.activate()) {
                    businessLogicSpan.setName("Get All Restaurants With Feedbacks");

                    List<Restaurant> topRestaurantsModel = restaurantService.getAllRestaurantsWithFeedbacks(topRestaurants);
                    List<Restaurant> top6RestaurantsModel = topRestaurantsModel.stream().limit(6).collect(Collectors.toList());
                    businessLogicSpan.end();

                    return ResponseEntity.ok().body(top6RestaurantsModel);
                } catch (Exception e) {
                    businessLogicSpan.captureException(e);
                    businessLogicSpan.end();
                    throw e;
                }
            } catch (Exception e) {
                dbSpan.captureException(e);
                dbSpan.end();
                throw e;
            }
        } catch (Exception e) {
            transaction.captureException(e);
            return ResponseEntity.badRequest().body(null);
        } finally {
            transaction.end();
        }
    }

    @GetMapping("/types")
    public ResponseEntity<List<String>> findRestaurantsTypes() {
        Transaction transaction = ElasticApm.startTransaction();
        try (Scope txScope = transaction.activate()) {
            transaction.setName("HTTP GET /types");
            transaction.setType(Transaction.TYPE_REQUEST);

            Span dbSpan = transaction.startSpan("db", "query", "findDistinctTypes");
            try (Scope dbScope = dbSpan.activate()) {
                dbSpan.setName("Find Distinct Types");

                List<String> types = restaurantRepository.findDistinctTypes();
                dbSpan.end();

                return ResponseEntity.ok().body(types);
            } catch (Exception e) {
                dbSpan.captureException(e);
                dbSpan.end();
                throw e;
            }
        } catch (Exception e) {
            transaction.captureException(e);
            return ResponseEntity.badRequest().body(null);
        } finally {
            transaction.end();
        }
    }

    @GetMapping("/location")
    public ResponseEntity<Map<String, List<String>>> findRestaurantsLocation() {
        Transaction transaction = ElasticApm.startTransaction();
        try (Scope txScope = transaction.activate()) {
            transaction.setName("HTTP GET /location");
            transaction.setType(Transaction.TYPE_REQUEST);

            Span businessLogicSpan = transaction.startSpan("custom", "logic", "getCountriesAndCities");
            try (Scope businessLogicScope = businessLogicSpan.activate()) {
                businessLogicSpan.setName("Get Countries And Cities");

                Map<String, List<String>> countryCityMap = restaurantService.getCountriesAndCities();
                businessLogicSpan.end();

                return ResponseEntity.ok().body(countryCityMap);
            } catch (Exception e) {
                businessLogicSpan.captureException(e);
                businessLogicSpan.end();
                throw e;
            }
        } catch (Exception e) {
            transaction.captureException(e);
            return ResponseEntity.badRequest().body(null);
        } finally {
            transaction.end();
        }
    }

    @GetMapping("/typesCountriesAndCities")
    public ResponseEntity<Map<String, Map<String, List<String>>>> findTypesCountriesAndCities() {
        Transaction transaction = ElasticApm.startTransaction();
        try (Scope txScope = transaction.activate()) {
            transaction.setName("HTTP GET /typesCountriesAndCities");
            transaction.setType(Transaction.TYPE_REQUEST);

            Span businessLogicSpan = transaction.startSpan("custom", "logic", "getTypesCountriesAndCities");
            try (Scope businessLogicScope = businessLogicSpan.activate()) {
                businessLogicSpan.setName("Get Types Countries And Cities");

                Map<String, Map<String, List<String>>> typeCountryCityMap = restaurantService.getTypesCountriesAndCities();
                businessLogicSpan.end();

                return ResponseEntity.ok().body(typeCountryCityMap);
            } catch (Exception e) {
                businessLogicSpan.captureException(e);
                businessLogicSpan.end();
                throw e;
            }
        } catch (Exception e) {
            transaction.captureException(e);
            return ResponseEntity.badRequest().body(null);
        } finally {
            transaction.end();
        }
    }

    @PostMapping("/reviews")
    public ResponseEntity<String> addOrUpdateReview(
            @RequestParam String username,
            @RequestParam String restaurantName,
            @RequestParam(required = false) Double foodRate,
            @RequestParam(required = false) Double serviceRate,
            @RequestParam(required = false) Double ambianceRate,
            @RequestParam(required = false) Double overallRate,
            @RequestParam(required = false) String comment) {
        Transaction transaction = ElasticApm.startTransaction();
        try (Scope txScope = transaction.activate()) {
            transaction.setName("HTTP POST /reviews");
            transaction.setType(Transaction.TYPE_REQUEST);

            if (foodRate == null && serviceRate == null && ambianceRate == null && overallRate == null && comment == null) {
                return ResponseEntity.ok("Review parameters are valid, but review update not requested");
            }

            Span dbSpan = transaction.startSpan("db", "query", "findByCustomerUsernameAndRestaurantName");
            try (Scope dbScope = dbSpan.activate()) {
                dbSpan.setName("Find Feedback By Customer Username And Restaurant Name");

                FeedbackEntity feedbackEntity = feedbackRepository.findByCustomerUsernameAndRestaurantName(username, restaurantName);
                dbSpan.end();

                if (feedbackEntity == null) {
                    LocalDateTime localDateTime = LocalDateTime.now();
                    FeedbackEntity newFeedbackEntity = new FeedbackEntity(clientRepository.findByUsername(username),
                            restaurantRepository.findByName(restaurantName), foodRate, serviceRate, ambianceRate, overallRate, comment, localDateTime);

                    Span saveSpan = transaction.startSpan("db", "query", "saveFeedbackEntity");
                    try (Scope saveScope = saveSpan.activate()) {
                        saveSpan.setName("Save New Feedback Entity");
                        feedbackRepository.save(newFeedbackEntity);
                        saveSpan.end();
                    }

                    Span updateSpan = transaction.startSpan("custom", "logic", "updateRestaurantRatings");
                    try (Scope updateScope = updateSpan.activate()) {
                        updateSpan.setName("Update Restaurant Ratings");
                        restaurantService.updateRestaurantRatings(restaurantName, foodRate, serviceRate, ambianceRate, overallRate);
                        updateSpan.end();
                    }
                } else {
                    if (foodRate != null && serviceRate != null && ambianceRate != null && overallRate != null && comment != null) {
                        Span updateSpan = transaction.startSpan("db", "query", "updateExistingFeedback");
                        try (Scope updateScope = updateSpan.activate()) {
                            updateSpan.setName("Update Existing Feedback");
                            feedbackRepository.updateExistingFeedback(username, restaurantName, foodRate, serviceRate, ambianceRate, overallRate, comment);
                            updateSpan.end();
                        }

                        Span ratingUpdateSpan = transaction.startSpan("custom", "logic", "updateRestaurantRatings");
                        try (Scope ratingUpdateScope = ratingUpdateSpan.activate()) {
                            ratingUpdateSpan.setName("Update Restaurant Ratings");
                            restaurantService.updateRestaurantRatings(restaurantName, foodRate, serviceRate, ambianceRate, overallRate);
                            ratingUpdateSpan.end();
                        }
                    }
                }

                return ResponseEntity.ok("Review added/updated successfully");
            } catch (Exception e) {
                dbSpan.captureException(e);
                dbSpan.end();
                throw e;
            }
        } catch (Exception e) {
            transaction.captureException(e);
            return ResponseEntity.status(400).body("Failed to add/update review: " + e.getMessage());
        } finally {
            transaction.end();
        }
    }

}
