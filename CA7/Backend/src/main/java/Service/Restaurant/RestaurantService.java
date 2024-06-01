package Service.Restaurant;

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
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RestaurantService {


    @Autowired
    RestaurantRepository restaurantRepository;
    @Autowired
    FeedbackRepository feedbackRepository;
    @Autowired
    ClientRepository clientRepository;
    @Autowired
    private AddressRestaurantRepository addressRestaurantRepository;

    public List < Restaurant > getAllRestaurantsWithFeedbacks ( List < RestaurantEntity > restaurantEntities ) {
        List < Restaurant > restaurantModels = new ArrayList <> ( );

        for ( RestaurantEntity restaurantEntity : restaurantEntities ) {
            List < FeedbackEntity > feedbackEntities = feedbackRepository.findByRestaurantId ( restaurantEntity.getId ( ) );
            List < Feedback > feedbackModels = new ArrayList <> ( );

            for ( FeedbackEntity feedbackEntity : feedbackEntities ) {
                Feedback feedbackModel = new Feedback ( );
                feedbackModel.setAmbianceRate ( feedbackEntity.getAmbianceRate ( ) );
                feedbackModel.setServiceRate ( feedbackEntity.getServiceRate ( ) );
                feedbackModel.setComment ( feedbackEntity.getComment ( ) );
                feedbackModel.setRestaurantName ( feedbackEntity.getRestaurant ( ).getName ( ) );
                feedbackModel.setFoodRate ( feedbackEntity.getFoodRate ( ) );
                feedbackModel.setDateTime ( feedbackEntity.getDateTime ( ) );
                feedbackModel.setOverallRate ( feedbackEntity.getOverallRate ( ) );
                feedbackModel.setUsername ( feedbackEntity.getCustomer ( ).getUsername ( ) );
                feedbackModels.add ( feedbackModel );
            }

            Restaurant restaurantModel = new Restaurant ( );
            restaurantModel.setId ( restaurantEntity.getId ( ) );
            restaurantModel.setServiceAvg ( restaurantEntity.getServiceAvg ( ) );
            restaurantModel.setOverallAvg ( restaurantEntity.getOverallAvg ( ) );
            restaurantModel.setFoodAvg ( restaurantEntity.getFoodAvg ( ) );
            restaurantModel.setAmbianceAvg ( restaurantEntity.getAmbianceAvg ( ) );
            restaurantModel.setDescription ( restaurantEntity.getDescription ( ) );
            restaurantModel.setEndTime ( restaurantEntity.getEndTime ( ) );
            restaurantModel.setStartTime ( restaurantEntity.getStartTime ( ) );
            restaurantModel.setImage ( restaurantEntity.getImage ( ) );
            restaurantModel.setManagerUsername ( restaurantEntity.getName ( ) );
            restaurantModel.setType ( restaurantEntity.getType ( ) );
            restaurantModel.setName ( restaurantEntity.getName ( ) );
            restaurantModel.setReservations ( new ArrayList <> ( ) );/////////////////
            restaurantModel.setTables ( new ArrayList <> ( ) );////////////
            AddressRestaurant addressRestaurant = new AddressRestaurant ( );
            addressRestaurant.setCity ( restaurantEntity.getAddress ( ).getCity ( ) );
            addressRestaurant.setCountry ( restaurantEntity.getAddress ( ).getCountry ( ) );
            addressRestaurant.setStreet ( restaurantEntity.getAddress ( ).getStreet ( ) );
            restaurantModel.setAddress ( addressRestaurant );
            restaurantModel.setFeedbacks ( feedbackModels );
            restaurantModels.add ( restaurantModel );
        }

        return restaurantModels;
    }

    public Map < String, List < String > > getCountriesAndCities ( ) {
        List < Object[] > countryCityPairs = addressRestaurantRepository.findDistinctCountriesAndCities ( );
        Map < String, List < String > > countryCityMap = new HashMap <> ( );

        for ( Object[] pair : countryCityPairs ) {
            String country = ( String ) pair[ 0 ];
            String city = ( String ) pair[ 1 ];

            countryCityMap.computeIfAbsent ( country , k -> new ArrayList <> ( ) ).add ( city );
        }

        return countryCityMap;
    }

    public Map < String, Map < String, List < String > > > getTypesCountriesAndCities ( ) {
        List < String > types = restaurantRepository.findDistinctTypes ( );
        Map < String, Map < String, List < String > > > typeCountryCityMap = new HashMap <> ( );

        for ( String type : types ) {
            List < Object[] > countryCityPairs = addressRestaurantRepository.findDistinctCountriesAndCities ( );
            Map < String, List < String > > countryCityMap = new HashMap <> ( );

            for ( Object[] pair : countryCityPairs ) {
                String country = ( String ) pair[ 0 ];
                String city = ( String ) pair[ 1 ];

                countryCityMap.computeIfAbsent ( country , k -> new ArrayList <> ( ) ).add ( city );
            }

            typeCountryCityMap.put ( type , countryCityMap );
        }

        return typeCountryCityMap;
    }

    @Transactional
    public void updateRestaurantRatings ( String restaurantName , Double foodRate , Double serviceRate ,
                                          Double ambianceRate , Double overallRate ) {
        try {
            RestaurantEntity restaurant = restaurantRepository.findByName ( restaurantName );

            if ( restaurant != null ) {

                List < FeedbackEntity > feedbacks = feedbackRepository.findByRestaurantId ( restaurant.getId ( ) );

                System.out.println ( restaurant.getFoodAvg ( ) );

                double newFoodAvg = calculateNewAverage ( restaurant.getFoodAvg ( ) , feedbacks.size ( ) , foodRate );
                double newServiceAvg = calculateNewAverage ( restaurant.getServiceAvg ( ) , feedbacks.size ( ) , serviceRate );
                double newAmbianceAvg = calculateNewAverage ( restaurant.getAmbianceAvg ( ) , feedbacks.size ( ) , ambianceRate );
                double newOverallAvg = calculateOverallAverage ( newFoodAvg , newServiceAvg , newAmbianceAvg );

                // Update restaurant averages
                restaurant.setFoodAvg ( newFoodAvg );
                restaurant.setServiceAvg ( newServiceAvg );
                restaurant.setAmbianceAvg ( newAmbianceAvg );
                restaurant.setOverallAvg ( newOverallAvg );

                restaurantRepository.save ( restaurant );
            }
        } catch ( Exception e ) {
            e.printStackTrace ( );
        }
    }

    private double calculateNewAverage ( double currentAvg , int currentCount , double newRating ) {
        return ( currentAvg * currentCount + newRating ) / ( currentCount + 1 );
    }

    private double calculateOverallAverage ( double foodAvg , double serviceAvg , double ambianceAvg ) {
        return ( foodAvg + serviceAvg + ambianceAvg ) / 3.0;
    }

}
