package domain.exception;

public class throwWrongManagerRoleException {
    public throwWrongManagerRoleException() throws Exception {
        throw new Exception("The Manager must have manger role but is client.");
    }
}
