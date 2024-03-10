package Controller;

import Model.Exception.ExceptionMessages;
import Model.Exception.SuperException;
import Model.Restaurant.Restaurant;
import Model.Table.Table;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
public class RestaurantController {
    private MizDooni mizDooni = MizDooni.getInstance ( );
    List <Restaurant> restaurants = mizDooni.getRestaurants();
    private static final List < String > AVAILABLE_TIMES = new ArrayList <> ( );
    private static final List < String > AVAILABLE_TYPES = List.of ( "Iranian" , "Asian" , "Arabian" , "Italian" , "Fast Food" );

    static {
        for ( int i = 0 ; i < 24 ; i++ ) {
            AVAILABLE_TIMES.add ( String.format ( "%02d:00" , i ) );
        }
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

//    public void addRestaurant ( String args ) throws Exception {
//        JSONObject jsonObject = ( JSONObject ) new JSONParser ( ).parse ( args );
//        String name = ( String ) jsonObject.get ( "name" );
//        validateRestaurantName ( name );
//
//        String manager = ( String ) jsonObject.get ( "managerUsername" );
//        validateManager ( manager );
//
//        String type = ( String ) jsonObject.get ( "type" );
//        validateType ( type );
//
//        String description = ( String ) jsonObject.get ( "description" );
//
//        String startTime = ( String ) jsonObject.get ( "startTime" );
//        String endTime = ( String ) jsonObject.get ( "endTime" );
//        validateTime ( startTime , endTime );
//
//        JSONObject addressObject = ( JSONObject ) jsonObject.get ( "address" );
//        validateAddress ( addressObject );
//
//        AddressRestaurant address = new AddressRestaurant ( ( String ) addressObject.get ( "country" ) ,
//                ( String ) addressObject.get ( "city" ) , ( String ) addressObject.get ( "street" ) );
//
//        Restaurant restaurant = new Restaurant ( name , manager , type , startTime , endTime , description , address );
//        mizDooni.addRestaurant ( restaurant );
//    }

//    public List < Restaurant > parseSearchByNameArgs ( String args ) throws Exception {
//        JSONObject jsonObject = ( JSONObject ) new JSONParser ( ).parse ( args );
//        String name = ( String ) jsonObject.get ( "name" );
//        doesRestaurantExists ( name );
//        return mizDooni.getRestaurantsByName ( name );
//    }

//    public List < Restaurant > parseSearchByTypeArgs ( String args ) throws Exception {
//        JSONObject jsonObject = ( JSONObject ) new JSONParser ( ).parse ( args );
//        String type = ( String ) jsonObject.get ( "type" );
////        validateType ( type );
//        List < Restaurant > restaurants = mizDooni.getRestaurantsByType ( type );
//        if ( restaurants.isEmpty ( ) ) {
//            new throwTypeNotExistsException ( );
//        }
//        return restaurants;
//    }

//    private void validateTime ( String startTime , String endTime ) throws Exception {
//        if ( ! AVAILABLE_TIMES.contains ( startTime ) || ! AVAILABLE_TIMES.contains ( endTime ) ) {
//            new throwWrongTimeException ( );
//        }
//    }

//    private void validateRestaurantName ( String name ) throws Exception {
//        if ( mizDooni.isRestaurantNameExists ( name ) ) {
//            new throwRestaurantNameExistsException ( );
//        }
//    }

//    private void validateManager ( String username ) throws Exception {
//        if ( ! mizDooni.isUserExists ( username ) ) {
//            new throwUsernameNotExistsException ( );
//        }
//        if ( ! mizDooni.isManager ( username ) ) {
//            new throwWrongManagerRoleException ( );
//        }
//    }

//    private void validateAddress ( JSONObject addressObject ) throws Exception {
//        if ( ! isValidAddress ( addressObject ) ) {
//            new throwRestaurantAddressException ( );
//        }
//    }

//    private boolean isValidAddress ( JSONObject addressObject ) {
//        return addressObject != null && addressObject.containsKey ( "country" ) && addressObject.containsKey ( "city" )
//                && addressObject.containsKey ( "street" );
//    }

//    private void validateType ( String type ) throws Exception {
//        if ( ! AVAILABLE_TYPES.contains ( type ) ) {
//            new throwWrongTypeException ( );
//        }
//    }

//    private void doesRestaurantExists ( String name ) throws Exception {
//        if ( ! mizDooni.isRestaurantNameAvailable ( name ) ) {
//            new throwRestaurantNameNotExistsException ( );
//        }
//    }

//    public void parseAddTable ( String args ) throws Exception {
//        JSONObject jsonObject = ( JSONObject ) new JSONParser ( ).parse ( args );
//        String restaurantName = ( String ) jsonObject.get ( "restaurantName" );
//        int tableNumber = ( ( Long ) jsonObject.get ( "tableNumber" ) ).intValue ( );
//        int seatsNumber = ( ( Long ) jsonObject.get ( "seatsNumber" ) ).intValue ( );
//        String managerUsername = ( String ) jsonObject.get ( "managerUsername" );
//        validateTable ( restaurantName , tableNumber , seatsNumber , managerUsername );
//        Restaurant restaurant = mizDooni.getRestaurantByName ( restaurantName );
//        Table table = new Table ( tableNumber , restaurantName , managerUsername , seatsNumber );
//        mizDooni.addTable ( restaurant , table );
//    }

//    private void validateTable ( String restaurantName , int tableNumber , int seatsNumber , String managerUsername ) throws Exception {
//        Restaurant restaurant = mizDooni.getRestaurantByName ( restaurantName );
//        doesRestaurantExists ( restaurant.getName ( ) );
//        validateTableNum ( restaurant.getTables ( ) , tableNumber );
//        validateSeatsNumber ( seatsNumber );
//    }

    private boolean isValidTableNum ( List < Table > tables , int tableNumber ) {
        for ( Table table : tables ) {
            if ( table.getTableNumber ( ) == tableNumber ) {
                return false;
            }
        }
        return true;
    }

//    private void validateTableNum ( List < Table > tables , int tableNumber ) throws Exception {
//        if ( ! isValidTableNum ( tables , tableNumber ) ) {
//            new throwTableNumAlreadyExistsException ( );
//        }
//    }
//
//    private void validateSeatsNumber ( int seatsNumber ) throws Exception {
//        if ( seatsNumber <= 0 || seatsNumber != ( int ) seatsNumber ) {
//            new throwInvalidSeatsNumberException ( );
//        }
//    }

//    public void parseArgAdd ( String args ) throws Exception {
//        JSONObject jsonObject = (JSONObject) new JSONParser().parse(args);
//        String name = (String) jsonObject.get("name");
////        validateRestaurantName(name);
//
//        String manager = (String) jsonObject.get("managerUsername");
////        validateManager(manager);
//
//        String type = (String) jsonObject.get("type");
////        validateType(type);
//
//        String description = (String) jsonObject.get("description");
//
//        String startTime = (String) jsonObject.get("startTime");
//        String endTime = (String) jsonObject.get("endTime");
////        validateTime(startTime, endTime);
//
//        JSONObject addressObject = (JSONObject) jsonObject.get("address");
////        validateAddress(addressObject);
//
//        AddressRestaurant address = new AddressRestaurant((String) addressObject.get("country"),
//                (String) addressObject.get("city"), (String) addressObject.get("street"));
//
//        Restaurant restaurant = new Restaurant(name, manager, type, startTime, endTime, description, address);
//    }
//    public int parseArgReserveTable ( String args ) throws Exception {
//        JSONObject jsonObject = ( JSONObject ) new JSONParser ( ).parse ( args );
//        String username = ( String ) jsonObject.get ( "username" );
//        String restaurantName = ( String ) jsonObject.get ( "restaurantName" );
//        int tableNumber = ( ( Long ) jsonObject.get ( "tableNumber" ) ).intValue ( );
//        String datetimeString = ( String ) jsonObject.get ( "datetime" );
//        if ( ! mizDooni.isRestaurantNameExists ( restaurantName ) ) {
//            new throwRestaurantNameNotExistsException ( );
//        }
//        if ( ! mizDooni.isUserExists ( username ) ) {
//            new throwUsernameNotExistsException ( );
//        } else {
//            User user = mizDooni.getUserByUsername ( username );
//            if ( user.getRole ( ).equals ( "manager" ) ) {
//                new throwNotAllowedReservationException ( );
//            }
//        }
//        LocalDateTime datetime = parseDateTime ( datetimeString );
//        // Check if the minutes and seconds are zero
//        if ( datetime.getMinute ( ) != 0 || datetime.getSecond ( ) != 0 ) {
//            new throwWrongTimeException ( );
//        }
//        Restaurant restaurant = mizDooni.getRestaurantByName ( restaurantName );
//        LocalTime startTime = LocalTime.parse ( restaurant.getStartTime ( ) );
//        LocalTime endTime = LocalTime.parse ( restaurant.getEndTime ( ) );
//        LocalTime reservationTime = datetime.toLocalTime ( );
//        if ( reservationTime.isBefore ( startTime ) || reservationTime.isAfter ( endTime ) ) {
//            new throwOutOfWorkingHour ( );
//        }
//
//        if ( datetime.isBefore ( LocalDateTime.now ( ) ) ) {
//            new throwPastDateTimeException ( );
//        }
//
//
//        //Restaurant restaurant = mizDooni.getRestaurantByName(restaurantName);
//        List < Table > tables = restaurant.getTables ( );
//        boolean tableExists = false;
//        for ( Table table : tables ) {
//            if ( table.getTableNumber ( ) == tableNumber ) {
//                tableExists = true;
//                break;
//            }
//        }
//        if ( ! tableExists ) {
//            new throwTableNotFoundException ( );
//        }
//
//        if ( isTableReserved ( restaurantName , tableNumber , datetime ) ) {
//            new throwTableAlreadyReservedException ( );
//        }
//
//        int reservationNumber = mizDooni.getReservationNumber ( );
//        Reservation reservation = new Reservation ( username , restaurantName , tableNumber , reservationNumber , datetime );
//        restaurant.addReservation ( reservation );
//        User user = mizDooni.getUserByUsername ( username );
//        user.addReservation ( reservation );
//        mizDooni.setReservationNumber ( reservationNumber + 1 );
//        return mizDooni.getReservationNumber ( ) - 1;
//    }

//    private LocalDateTime parseDateTime ( String datetimeString ) throws Exception {
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern ( "yyyy-MM-dd HH:mm" );
//        TemporalAccessor temporalAccessor = formatter.parseBest ( datetimeString , LocalDateTime :: from , LocalDate :: from );
//        if ( temporalAccessor instanceof LocalDateTime ) {
//            return ( LocalDateTime ) temporalAccessor;
//        } else {
//            new throwInvalidDateTimeFormatException ( );
//        }
//        return null;
//    }

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
//    private boolean isTableReserved ( String restaurantName , int tableNumber , LocalDateTime datetime ) {
//        Restaurant restaurant = mizDooni.getRestaurantByName ( restaurantName );
//        if ( restaurant == null || restaurant.getReservations ( ) == null ) {
//            return false;
//        }
//
//        List < Reservation > reservations = restaurant.getReservations ( );
//        if ( reservations.isEmpty ( ) ) {
//            return false;
//        }
//
//        for ( Reservation reservation : reservations ) {
//            if ( reservation.getTableNumber ( ) == tableNumber && reservation.getDatetime ( ).equals ( datetime ) ) {
//                return true;
//            }
//        }
//
//        return false;
//    }

//    public void cancelReservation ( String args ) throws Exception {
//        JSONObject jsonObject = ( JSONObject ) new JSONParser ( ).parse ( args );
//        String username = ( String ) jsonObject.get ( "username" );
//        int reservationNumber = ( ( Long ) jsonObject.get ( "reservationNumber" ) ).intValue ( );
//
//        User user = mizDooni.getUserByUsername ( username );
//        if ( user == null ) {
//            new throwUsernameNotExistsException ( );
//        }
//
//        Reservation reservationToRemove = null;
//        for ( Reservation reservation : user.getReservations ( ) ) {
//            if ( reservation.getReservationNumber ( ) == reservationNumber ) {
//                reservationToRemove = reservation;
//                break;
//            }
//        }
//
//        if ( reservationToRemove == null ) {
//            new throwReservationNotFoundException ( );
//        }
//
//        LocalDateTime currentDateTime = LocalDateTime.now ( );
//        if ( reservationToRemove.getDatetime ( ).isBefore ( currentDateTime ) ) {
//            new throwPastDateTimeException ( );
//        }
//
//        user.getReservations ( ).remove ( reservationToRemove );
//        for ( Restaurant restaurant : mizDooni.getRestaurants ( ) ) {
//            List < Reservation > reservations = restaurant.getReservations ( );
//
//            reservationToRemove = null;
//            for ( Reservation reservation : reservations ) {
//                if ( reservation.getUsername ( ).equals ( username ) && reservation.getReservationNumber ( ) == reservationNumber ) {
//                    reservationToRemove = reservation;
//                    break;
//                }
//            }
//
//            if ( reservationToRemove != null ) {
//                reservations.remove ( reservationToRemove );
//                return;
//            }
//        }
//
//        new throwReservationNotFoundException ( );
//    }
//

//    public ObjectNode showAvailableTables ( String args ) throws Exception {
//        ObjectMapper objectMapper = new ObjectMapper ( );
//        ObjectNode response = objectMapper.createObjectNode ( );
//        ArrayNode availableTablesArray = response.putArray ( "availableTables" );
//
//        JSONObject jsonObject = ( JSONObject ) new JSONParser ( ).parse ( args );
//        String restaurantName = ( String ) jsonObject.get ( "restaurantName" );
//        Restaurant restaurant = mizDooni.getRestaurantByName ( restaurantName );
//
//        LocalDate today = LocalDate.now ( );
//        LocalDate tomorrow = today.plusDays ( 1 );
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern ( "yyyy-MM-dd" );
//
//        LocalTime startTime = LocalTime.parse ( restaurant.getStartTime ( ) );
//        LocalTime endTime = LocalTime.parse ( restaurant.getEndTime ( ) );
//
//
//        for ( Table table : restaurant.getTables ( ) ) {
//
//
//            ObjectNode tableInfo = objectMapper.createObjectNode ( );
//            tableInfo.put ( "tableNumber" , table.getTableNumber ( ) );
//            tableInfo.put ( "seatsNumber" , table.getSeatsNumber ( ) );
//
//            ArrayNode availableTimesArray = objectMapper.createArrayNode ( );
//
//            for ( LocalTime time = startTime ; ! time.equals ( endTime.plusHours ( 1 ) ) ; time = time.plusHours ( 1 ) ) {
//
//                LocalDateTime todayDateTime = LocalDateTime.of ( today , time );
//                LocalDateTime tomorrowDateTime = LocalDateTime.of ( tomorrow , time );
//
//                if ( ! isTableReserved ( restaurantName , table.getTableNumber ( ) , todayDateTime )
//                        && ! todayDateTime.toLocalTime ( ).isBefore ( startTime )
//                        && ! todayDateTime.toLocalTime ( ).isAfter ( endTime ) ) {
//                    availableTimesArray.add ( today.format ( formatter ) + " " + time );
//                }
//                if ( ! isTableReserved ( restaurantName , table.getTableNumber ( ) , tomorrowDateTime )
//                        && ! tomorrowDateTime.toLocalTime ( ).isBefore ( startTime )
//                        && ! tomorrowDateTime.toLocalTime ( ).isAfter ( endTime ) ) {
//                    availableTimesArray.add ( tomorrow.format ( formatter ) + " " + time );
//                }
//            }
//
//            tableInfo.set ( "availableTimes" , availableTimesArray );
//
//            availableTablesArray.add ( tableInfo );
//        }
//
//        return response;
//    }


}
