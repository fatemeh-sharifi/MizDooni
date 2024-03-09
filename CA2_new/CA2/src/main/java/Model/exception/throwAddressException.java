package Model.exception;

public class throwAddressException {
    public throwAddressException() throws Exception {
        throw new Exception("The address must contain city and country.\n");
    }
}