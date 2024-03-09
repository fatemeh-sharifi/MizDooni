package model.exception;



public class throwEmailException {
    public throwEmailException() throws Exception {
        throw new Exception("The email must be in the format abc@example.com.\n");
    }
}