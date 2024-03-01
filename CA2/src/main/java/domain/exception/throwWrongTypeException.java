package domain.exception;

public class throwWrongTypeException {
    public throwWrongTypeException() throws Exception {
        throw new Exception("The restaurant type must be Iranian,Fast Food,Italian,Asian,Arabian.\n");
    }
}
