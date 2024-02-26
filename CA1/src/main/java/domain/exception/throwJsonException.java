package domain.exception;

public class throwJsonException {
    public throwJsonException(String key) throws Exception {
        throw new Exception("Key '" + key + "' not found in JSON object.");
    }
}
