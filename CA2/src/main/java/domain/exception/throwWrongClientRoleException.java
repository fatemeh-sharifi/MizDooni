package domain.exception;

public class throwWrongClientRoleException {
    public throwWrongClientRoleException() throws Exception {
        throw new Exception("The role must be client.");
    }
}
