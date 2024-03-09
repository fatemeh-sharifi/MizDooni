package Model.exception;
public class throwReservationNotFoundException {
    public throwReservationNotFoundException() throws Exception {
        throw new Exception("Reservation not found for the user.\n");
    }
}
