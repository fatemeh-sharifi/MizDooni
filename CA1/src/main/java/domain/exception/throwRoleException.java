package domain.exception;



public class throwRoleException {
    public throwRoleException() throws Exception {
        throw new Exception("The role must be manager or client!\n");
    }
}