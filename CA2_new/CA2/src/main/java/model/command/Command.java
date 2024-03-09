package model.command;

import java.util.Arrays;

public enum Command {
    ADD_USER("addUser"),
    ADD_RESTAURANT("addRestaurant"),
    ADD_TABLE("addTable"),
    RESERVE_TABLE("reserveTable"),
    CANCEL_RESERVATION("cancelReservation"),
    SHOW_RESERVATION_HISTORY("showReservationHistory"),
    SEARCH_RESTAURANTS_BY_NAME("searchRestaurantsByName"),
    SEARCH_RESTAURANTS_BY_TYPE("searchRestaurantsByType"),
    SHOW_AVAILABLE_TABLES("showAvailableTables"),
    ADD_REVIEW("addReview");

    private final String command;

    Command(String command) {
        this.command = command;
    }

    public static Command fromString(String text) {
        return Arrays.stream(values())
                .filter(command -> command.command.equalsIgnoreCase(text))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid command: " + text));
    }
}
