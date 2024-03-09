package model.exception;

public class throwNoReservationException {
    public throwNoReservationException() throws Exception {
        throw new Exception("You can not send feedback for this restaurant because no reservation is available for you.");
    }
}
