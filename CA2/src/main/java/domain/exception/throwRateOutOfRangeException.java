package domain.exception;

public class throwRateOutOfRangeException {
    public throwRateOutOfRangeException() throws Exception {
        throw new Exception("The rating must be in range of 0 to 5.");
    }
}
