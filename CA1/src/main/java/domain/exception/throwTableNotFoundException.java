package domain.exception;
public class throwTableNotFoundException {
    public throwTableNotFoundException() throws Exception {
        throw new Exception("Table not found in the restaurant.\n");
    }
}