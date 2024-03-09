package Model.exception;
public class throwTableAlreadyReservedException {
    public throwTableAlreadyReservedException() throws Exception {
        throw new Exception("Table is already reserved at the specified datetime.\n");
    }
}