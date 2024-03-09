package Model.exception;

public class throwEmailAlreadyExistsException {
    public throwEmailAlreadyExistsException() throws Exception {
        throw new Exception("Email address already exists.\n");
    }
}