package Model.exception;

public class throwRestaurantAddressException {
    public throwRestaurantAddressException() throws Exception {
        throw new Exception("The address must contain city,country and street.\n");
    }
}
