package Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import Model.Reservation.Reservation;
import Model.Restaurant.Restaurant;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class JsonController {
    private static final ObjectMapper objectMapper = new ObjectMapper ( );

    public static String generateSearchByNameJson ( List < Restaurant > restaurants ) throws Exception {
        ArrayNode restaurantArray = objectMapper.createArrayNode ( );
        for ( Restaurant restaurant : restaurants ) {
            ObjectNode restaurantObject = objectMapper.createObjectNode ( );
            restaurantObject.put ( "name" , restaurant.getName ( ) );
            restaurantObject.put ( "type" , restaurant.getType ( ) );
            restaurantObject.put ( "startTime" , restaurant.getStartTime ( ) );
            restaurantObject.put ( "endTime" , restaurant.getEndTime ( ) );
            restaurantObject.put ( "description" , restaurant.getDescription ( ) );
            ObjectNode addressObject = objectMapper.createObjectNode ( );
            addressObject.put ( "country" , restaurant.getAddress ( ).getCountry ( ) );
            addressObject.put ( "city" , restaurant.getAddress ( ).getCity ( ) );
            addressObject.put ( "street" , restaurant.getAddress ( ).getStreet ( ) );
            restaurantObject.set ( "address" , addressObject );
            restaurantArray.add ( restaurantObject );
        }
        ObjectNode dataObject = objectMapper.createObjectNode ( );
        dataObject.set ( "restaurants" , restaurantArray );
        ObjectNode responseObject = objectMapper.createObjectNode ( );
        responseObject.put ( "success" , true );
        responseObject.set ( "data" , dataObject );
        return objectMapper.writeValueAsString ( responseObject );
    }

    public static String generateSuccessJson ( String message ) throws Exception {
        ObjectNode response = objectMapper.createObjectNode ( );
        response.put ( "success" , true );
        response.put ( "data" , message );
        return objectMapper.writeValueAsString ( response );
    }

    public static String generateSuccessJsonAvailableTables ( ObjectNode availableTablesArray ) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper ( );
        ObjectNode response = objectMapper.createObjectNode ( );
        ObjectNode data = objectMapper.createObjectNode ( );
        data.set ( "availableTables" , availableTablesArray );
        response.put ( "success" , true );
        response.set ( "data" , data );
        return objectMapper.writeValueAsString ( response );
    }


    public static String generateSuccessJsonReserveTable ( int reservationNumber ) throws Exception {
        ObjectNode response = objectMapper.createObjectNode ( );
        response.put ( "success" , true );
        response.put ( "data" , "reservationNumber: " + reservationNumber );
        return objectMapper.writeValueAsString ( response );
    }

    public static String generateErrorJson ( String errorMessage ) throws Exception {
        ObjectNode response = objectMapper.createObjectNode ( );
        response.put ( "success" , false );
        response.put ( "data" , errorMessage );
        return objectMapper.writeValueAsString ( response );
    }

//    public static String generateHistoryJson ( List < Reservation > reservationHistory ) throws Exception {
//        ObjectMapper objectMapper = new ObjectMapper ( );
//        ArrayNode reservationArray = objectMapper.createArrayNode ( );
//        for ( Reservation reservation : reservationHistory ) {
//            ObjectNode reservationObject = objectMapper.createObjectNode ( );
//            reservationObject.put ( "reservationNumber" , reservation.getReservationNumber ( ) );
//            reservationObject.put ( "restaurantName" , reservation.getRestaurantName ( ) );
//            reservationObject.put ( "tableNumber" , reservation.getTableNumber ( ) );
//            LocalDateTime datetime = reservation.getDatetime ( );
//            String formattedDatetime = datetime.format ( DateTimeFormatter.ofPattern ( "yyyy-MM-dd HH:mm" ) );
//            reservationObject.put ( "datetime" , formattedDatetime );
//            reservationArray.add ( reservationObject );
//        }
//        ObjectNode dataObject = objectMapper.createObjectNode ( );
//        dataObject.set ( "reservationHistory" , reservationArray );
//        ObjectNode responseObject = objectMapper.createObjectNode ( );
//        responseObject.put ( "success" , true );
//        responseObject.set ( "data" , dataObject );
//        return objectMapper.writeValueAsString ( responseObject );
//    }
}
