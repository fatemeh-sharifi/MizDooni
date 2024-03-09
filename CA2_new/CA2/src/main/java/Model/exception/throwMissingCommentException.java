package Model.exception;

public class throwMissingCommentException {
    public throwMissingCommentException() throws Exception {
        throw new Exception("Feedback comment cannot be empty.\n");
    }
}