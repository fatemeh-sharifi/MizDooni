package domain.exception;

public class throwUsernameException {
    public throwUsernameException() throws Exception {
        throw new Exception("The username must not contain special characters.\n");
    }
}