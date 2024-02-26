package domain.exception;

public class throwRestaurantNameNotExistsException {
    public throwRestaurantNameNotExistsException() throws Exception {
        throw new Exception("The restaurant does not exists.");
    }
}
