package domain.exception;

public class throwRestaurantNameNotExistsException {
    public throwRestaurantNameNotExistsException() throws Exception {
        throw new Exception("The role must be manager or client.\n");
    }
}
