package domain.exception;

public class throwWrongTimeException {
    public throwWrongTimeException() throws Exception {
        throw new Exception("The time must be in order of 00:00,01:00,... .\n");
    }
}
