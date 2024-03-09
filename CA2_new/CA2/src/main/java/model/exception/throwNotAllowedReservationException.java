package model.exception;

public class throwNotAllowedReservationException {
    public throwNotAllowedReservationException() throws Exception {
        throw new Exception("Only clients are allowed to make reservations.\n");
    }
}