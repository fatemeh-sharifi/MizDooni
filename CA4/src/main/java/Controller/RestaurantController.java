package Controller;

import Model.Exception.ExceptionMessages;
import Model.Exception.SuperException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import Model.Address.AddressRestaurant;
import Model.Reservation.Reservation;
import Model.Restaurant.Restaurant;
import Model.Table.Table;
import Model.User.User;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import Service.MizDooni;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class RestaurantController {
    private MizDooni mizDooni = MizDooni.getInstance ( );
    private static final List < String > AVAILABLE_TIMES = new ArrayList <> ( );
    private static final List < String > AVAILABLE_TYPES = List.of ( "Iranian" , "Asian" , "Arabian" , "Italian" , "Fast Food" );

    static {
        for ( int i = 0 ; i < 24 ; i++ ) {
            AVAILABLE_TIMES.add ( String.format ( "%02d:00" , i ) );
        }
    }

    public void parseArgAdd ( String args ) throws Exception {
        JSONObject jsonObject = ( JSONObject ) new JSONParser ( ).parse ( args );
        String name = ( String ) jsonObject.get ( "name" );
        validateRestaurantName ( name );

        String manager = ( String ) jsonObject.get ( "managerUsername" );
        validateManager ( manager );

        String type = ( String ) jsonObject.get ( "type" );
        validateType ( type );

        String description = ( String ) jsonObject.get ( "description" );

        String startTime = ( String ) jsonObject.get ( "startTime" );
        String endTime = ( String ) jsonObject.get ( "endTime" );
        validateTime ( startTime , endTime );

        JSONObject addressObject = ( JSONObject ) jsonObject.get ( "address" );
        validateAddress ( addressObject );

        AddressRestaurant address = new AddressRestaurant ( ( String ) addressObject.get ( "country" ) ,
                ( String ) addressObject.get ( "city" ) , ( String ) addressObject.get ( "street" ) );

        Restaurant restaurant = new Restaurant ( name , manager , type , startTime , endTime , description , address );
        mizDooni.addRestaurant ( restaurant );
    }

    public List < Restaurant > parseSearchByNameArgs ( String args ) throws Exception {
        JSONObject jsonObject = ( JSONObject ) new JSONParser ( ).parse ( args );
        String name = ( String ) jsonObject.get ( "name" );
        doesRestaurantExists ( name );
        return mizDooni.getRestaurantsByName(name);
    }

    public List < Restaurant > parseSearchByTypeArgs ( String args ) throws Exception {
        JSONObject jsonObject = ( JSONObject ) new JSONParser ( ).parse ( args );
        String type = ( String ) jsonObject.get ( "type" );
        validateType ( type );
        List<Restaurant> restaurants;
        restaurants = mizDooni.getRestaurantsByType(type);
        if ( restaurants.isEmpty ( ) ) {
            throw new SuperException(ExceptionMessages.TYPE_NOT_EXISTS_EXCEPTION_MESSAGE);
        }
        return restaurants;
    }

    private void validateTime ( String startTime , String endTime ) throws Exception {
        if ( ! AVAILABLE_TIMES.contains ( startTime ) || ! AVAILABLE_TIMES.contains ( endTime ) ) {
            throw new SuperException (ExceptionMessages.OUT_OF_WORKING_HOUR_EXCEPTION_MESSAGE );
        }
    }

    private void validateRestaurantName ( String name ) throws Exception {
        if ( mizDooni.isRestaurantNameExists ( name ) ) {
            throw new SuperException (ExceptionMessages.RESTAURANT_NAME_NOT_EXISTS_EXCEPTION_MESSAGE );
        }
    }

    private void validateManager ( String username ) throws Exception {
        if ( ! mizDooni.isUserExists ( username ) ) {
            throw new SuperException (ExceptionMessages.USERNAME_NOT_EXISTS_EXCEPTION_MESSAGE );
        }
        if ( ! mizDooni.isManager ( username ) ) {
            throw new SuperException (ExceptionMessages.WRONG_MANAGER_ROLE_EXCEPTION_MESSAGE );
        }
    }

    private void validateAddress ( JSONObject addressObject ) throws Exception {
        if ( ! isValidAddress ( addressObject ) ) {
            throw new SuperException (ExceptionMessages.ADDRESS_EXCEPTION_MESSAGE );
        }
    }

    private boolean isValidAddress ( JSONObject addressObject ) {
        return addressObject != null && addressObject.containsKey ( "country" ) && addressObject.containsKey ( "city" )
                && addressObject.containsKey ( "street" );
    }

    private void validateType ( String type ) throws Exception {
        if ( ! AVAILABLE_TYPES.contains ( type ) ) {
            throw new SuperException (ExceptionMessages.WRONG_TYPE_EXCEPTION_MESSAGE );
        }
    }

    private void doesRestaurantExists ( String name ) throws Exception {
        if ( ! mizDooni.isRestaurantNameAvailable ( name ) ) {
            throw new SuperException (ExceptionMessages.RESTAURANT_NAME_NOT_EXISTS_EXCEPTION_MESSAGE );
        }
    }

    public void parseAddTable ( String args ) throws Exception {
        JSONObject jsonObject = ( JSONObject ) new JSONParser ( ).parse ( args );
        String restaurantName = ( String ) jsonObject.get ( "restaurantName" );
        int tableNumber = ( ( Long ) jsonObject.get ( "tableNumber" ) ).intValue ( );
        int seatsNumber = ( ( Long ) jsonObject.get ( "seatsNumber" ) ).intValue ( );
        String managerUsername = ( String ) jsonObject.get ( "managerUsername" );
        validateTable ( restaurantName , tableNumber , seatsNumber , managerUsername );
        Restaurant restaurant = mizDooni.getRestaurantByName ( restaurantName );
        Table table = new Table ( tableNumber , restaurantName , managerUsername , seatsNumber);
        mizDooni.addTable ( restaurant , table );
    }

    private void validateTable ( String restaurantName , int tableNumber , int seatsNumber , String managerUsername ) throws Exception {
        Restaurant restaurant = mizDooni.getRestaurantByName ( restaurantName );
        doesRestaurantExists ( restaurant.getName ( ) );
        validateTableNum ( restaurant.getTables ( ) , tableNumber );
        validateSeatsNumber ( seatsNumber );
    }

    private boolean isValidTableNum ( List < Table > tables , int tableNumber ) {
        for ( Table table : tables ) {
            if ( table.getTableNumber ( ) == tableNumber ) {
                return false;
            }
        }
        return true;
    }

    private void validateTableNum ( List < Table > tables , int tableNumber ) throws Exception {
        if ( ! isValidTableNum ( tables , tableNumber ) ) {
            throw new SuperException (ExceptionMessages.TABLE_NUM_ALREADY_EXISTS_EXCEPTION_MESSAGE );
        }
    }

    private void validateSeatsNumber ( int seatsNumber ) throws Exception {
        if ( seatsNumber <= 0 || seatsNumber != ( int ) seatsNumber ) {
            throw new SuperException (ExceptionMessages.INVALID_SEATS_NUMBER_EXCEPTION_MESSAGE );
        }
    }

//    public int parseArgReserveTable ( String args ) throws Exception {
//        JSONObject jsonObject = ( JSONObject ) new JSONParser ( ).parse ( args );
//        String username = ( String ) jsonObject.get ( "username" );
//        String restaurantName = ( String ) jsonObject.get ( "restaurantName" );
//        int tableNumber = ( ( Long ) jsonObject.get ( "tableNumber" ) ).intValue ( );
//        String datetimeString = ( String ) jsonObject.get ( "datetime" );
//        if ( ! mizDooni.isRestaurantNameExists ( restaurantName ) ) {
//            throw new SuperException (ExceptionMessages.RESERVATION_NOT_FOUND_EXCEPTION_MESSAGE );
//        }
//        if ( ! mizDooni.isUserExists ( username ) ) {
//            throw new SuperException (ExceptionMessages.USERNAME_NOT_EXISTS_EXCEPTION_MESSAGE );
//        } else {
//            User user = mizDooni.getUserByUsername ( username );
//            if ( user.getRole ( ).equals ( "manager" ) ) {
//                throw new SuperException (ExceptionMessages.NOT_ALLOWED_RESERVATION_EXCEPTION_MESSAGE );
//            }
//        }
//        LocalDateTime datetime = parseDateTime ( datetimeString );
//        // Check if the minutes and seconds are zero
//        if ( datetime.getMinute ( ) != 0 || datetime.getSecond ( ) != 0 ) {
//            throw new SuperException (ExceptionMessages.WRONG_TIME_EXCEPTION_MESSAGE );
//        }
//        Restaurant restaurant = mizDooni.getRestaurantByName ( restaurantName );
//        LocalTime startTime = LocalTime.parse ( restaurant.getStartTime ( ) );
//        LocalTime endTime = LocalTime.parse ( restaurant.getEndTime ( ) );
//        LocalTime reservationTime = datetime.toLocalTime ( );
//        if ( reservationTime.isBefore ( startTime ) || reservationTime.isAfter ( endTime ) ) {
//            throw new SuperException (ExceptionMessages.OUT_OF_WORKING_HOUR_EXCEPTION_MESSAGE );
//        }
//
//        if ( datetime.isBefore ( LocalDateTime.now ( ) ) ) {
//            throw new SuperException (ExceptionMessages.PAST_DATE_TIME_EXCEPTION_MESSAGE );
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
//            throw new SuperException (ExceptionMessages.TABLE_NOT_FOUND_EXCEPTION_MESSAGE );
//        }
//
//        if ( isTableReserved ( restaurantName , tableNumber , datetime ) ) {
//            throw new SuperException (ExceptionMessages.TABLE_ALREADY_RESERVED_EXCEPTION_MESSAGE );
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

    private LocalDateTime parseDateTime ( String datetimeString ) throws Exception {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern ( "yyyy-MM-dd HH:mm" );
        TemporalAccessor temporalAccessor = formatter.parseBest ( datetimeString , LocalDateTime :: from , LocalDate :: from );
        if ( temporalAccessor instanceof LocalDateTime ) {
            return ( LocalDateTime ) temporalAccessor;
        } else {
            throw new SuperException (ExceptionMessages.INVALID_DATE_TIME_FORMAT_EXCEPTION_MESSAGE );
        }
        //return null;
    }

    private boolean isTableReserved ( String restaurantName , int tableNumber , LocalDate date) {
        Restaurant restaurant = mizDooni.getRestaurantByName ( restaurantName );
        if ( restaurant == null || restaurant.getReservations ( ) == null ) {
            return false;
        }

        List < Reservation > reservations = restaurant.getReservations ( );
        if ( reservations.isEmpty ( ) ) {
            return false;
        }

        for ( Reservation reservation : reservations ) {
            if ( reservation.getTableNumber ( ) == tableNumber && reservation.getDate ( ).equals ( date ) ) {
                return true;
            }
        }

        return false;
    }

    public void cancelReservation ( String args ) throws Exception {
        JSONObject jsonObject = ( JSONObject ) new JSONParser ( ).parse ( args );
        String username = ( String ) jsonObject.get ( "username" );
        int reservationNumber = ( ( Long ) jsonObject.get ( "reservationNumber" ) ).intValue ( );

        User user = mizDooni.getUserByUsername ( username );
        if ( user == null ) {
            throw new SuperException (ExceptionMessages.USERNAME_NOT_EXISTS_EXCEPTION_MESSAGE );
        }

        Reservation reservationToRemove = null;
        for ( Reservation reservation : user.getReservations ( ) ) {
            if ( reservation.getReservationNumber ( ) == reservationNumber ) {
                reservationToRemove = reservation;
                break;
            }
        }

        if ( reservationToRemove == null ) {
            throw new SuperException (ExceptionMessages.RESERVATION_NOT_FOUND_EXCEPTION_MESSAGE );
        }

        LocalDate currentDate = LocalDate.now ( );
        if ( reservationToRemove.getDate ( ).isBefore ( currentDate ) ) {
            throw new SuperException (ExceptionMessages.PAST_DATE_TIME_EXCEPTION_MESSAGE );
        }

        user.getReservations ( ).remove ( reservationToRemove );
        for ( Restaurant restaurant : mizDooni.getRestaurants ( ) ) {
            List < Reservation > reservations = restaurant.getReservations ( );

            reservationToRemove = null;
            for ( Reservation reservation : reservations ) {
                if ( reservation.getUsername ( ).equals ( username ) && reservation.getReservationNumber ( ) == reservationNumber ) {
                    reservationToRemove = reservation;
                    break;
                }
            }

            if ( reservationToRemove != null ) {
                reservations.remove ( reservationToRemove );
                return;
            }
        }

        throw new SuperException (ExceptionMessages.RESERVATION_NOT_FOUND_EXCEPTION_MESSAGE );
    }


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
//                LocalDate todayDate =  today ;
//                LocalDate tomorrowDate = tomorrow ;
//
//                if ( ! isTableReserved ( restaurantName , table.getTableNumber ( ) , todayDate )
//                        && ! todayDate.toLocalT ( ).isBefore ( startTime )
//                        && ! todayDateTime.toLocalTime ( ).isAfter ( endTime ) ) {
//                    availableTimesArray.add ( today.format ( formatter ) + " " + time );
//                }
//                if ( ! isTableReserved ( restaurantName , table.getTableNumber ( ) , tomorrowDate )
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
