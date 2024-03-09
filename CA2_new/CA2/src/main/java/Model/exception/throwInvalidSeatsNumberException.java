package Model.exception;

public class throwInvalidSeatsNumberException {
    public throwInvalidSeatsNumberException() throws Exception{
        throw new Exception("The number of seats must be a positive integer.\n");
    }
}
