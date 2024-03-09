package model.exception;

public class throwPastDateTimeException {
    public throwPastDateTimeException() throws Exception {
        throw new Exception("Reservation datetime should be in the future.\n");
    }
}