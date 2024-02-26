package domain.exception;
public class TableAlreadyReservedException {
    public TableAlreadyReservedException() throws Exception {
        throw new Exception("\"Table is already reserved at the specified datetime.\n");
    }
}