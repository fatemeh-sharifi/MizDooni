package model.exception;

public class throwOutOfWorkingHour {
    public throwOutOfWorkingHour() throws Exception {
        throw new Exception("The selected date and time is outside of the restaurant's working hours.\n");
    }
}