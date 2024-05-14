package Controller;

import com.fasterxml.jackson.databind.node.ObjectNode;
import Model.Command.Command;
import Model.Reservation.Reservation;
import Model.Restaurant.Restaurant;

import java.util.List;
import java.util.Scanner;

public class CommandHndlr {
    private final UserController userController = new UserController ( );
    private final RestaurantController restaurantController = new RestaurantController ( );
    private final JsonController jsonController = new JsonController ( );
    private final FeedbackController feedbackController = new FeedbackController ( );

    public void start ( ) throws Exception {
        System.out.println ( "Welcome!" );
        Scanner scanner = new Scanner ( System.in );
        while ( true ) {
            System.out.println ( "Enter a command (type 'q' to exit):" );
            String input = scanner.nextLine ( ).trim ( );
            if ( "q".equalsIgnoreCase ( input ) ) {
                break;
            }
            String[] parts = input.split ( "\\s+" , 2 );
            if ( parts.length != 2 ) {
                System.out.println ( "Invalid command format. Please try again." );
                continue;
            }
            String commandStr = parts[ 0 ];
            String args = parts[ 1 ];
            try {
                String response = executeCommand ( commandStr , args );
                System.out.println ( response );
            } catch ( Exception e ) {
                System.out.println ( jsonController.generateErrorJson ( e.getMessage ( ) ) );
            }
        }
    }

    private String executeCommand ( String commandStr , String args ) throws Exception {
        Command command = Command.fromString ( commandStr );
        switch ( command ) {
            case ADD_USER:
                userController.parseArgAdd ( args );
                return jsonController.generateSuccessJson ( "User added successfully." );
            case ADD_RESTAURANT:
                restaurantController.parseArgAdd ( args );
                return jsonController.generateSuccessJson ( "Restaurant added successfully." );
            case ADD_TABLE:
                restaurantController.parseAddTable ( args );
                return jsonController.generateSuccessJson ( "Table added successfully." );
            case RESERVE_TABLE:
                //int reservationNumber = restaurantController.parseArgReserveTable ( args );
                //return jsonController.generateSuccessJsonReserveTable ( reservationNumber );
            case CANCEL_RESERVATION:
                restaurantController.cancelReservation ( args );
                return jsonController.generateSuccessJson ( "Reservation cancelled successfully." );
            case SHOW_RESERVATION_HISTORY:
//                List < Reservation > reservations = userController.parseHistoryArgs ( args );
//                return jsonController.generateHistoryJson ( reservations );
            case SEARCH_RESTAURANTS_BY_NAME:
                List < Restaurant > restaurantsByName = restaurantController.parseSearchByNameArgs ( args );
                return jsonController.generateSearchByNameJson ( restaurantsByName );
            case SEARCH_RESTAURANTS_BY_TYPE:
                List < Restaurant > restaurantsByType = restaurantController.parseSearchByTypeArgs ( args );
                return jsonController.generateSearchByNameJson ( restaurantsByType );
            case SHOW_AVAILABLE_TABLES:
                //ObjectNode availableTables = restaurantController.showAvailableTables ( args );
                //return jsonController.generateSuccessJsonAvailableTables ( availableTables );
            case ADD_REVIEW:
                feedbackController.parseArgAdd ( args );
                return jsonController.generateSuccessJson ( "Review added successfully." );
            default:
                throw new IllegalArgumentException ( "Invalid command: " + commandStr );
        }
    }

}


