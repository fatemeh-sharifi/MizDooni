package model.exception;

public class throwInvalidDateTimeFormatException {
    public throwInvalidDateTimeFormatException() throws Exception {
        throw new Exception("Invalid datetime format. Please use yyyy-MM-dd HH:mm format.\n");
    }
}