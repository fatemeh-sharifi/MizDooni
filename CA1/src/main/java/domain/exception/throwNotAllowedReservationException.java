package domain.exception;

public class throwNotAllowedReservationException {
    public throwNotAllowedReservationException() throws Exception {
        throw new Exception("Only managers are allowed to make reservations.\n");
    }
}