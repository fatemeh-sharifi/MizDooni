package Model.exception;

public class throwTypeNotExistsException {
    public throwTypeNotExistsException() throws Exception {
        throw new Exception("No restaurant found with this type.");
    }
}
