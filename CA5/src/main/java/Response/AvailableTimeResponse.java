package Response;
import Model.Table.Table;

import java.util.List;

public class AvailableTimeResponse {
    private List<Integer> availableTimes;
    private Table table;

    public AvailableTimeResponse(List<Integer> availableTimes, Table table) {
        this.availableTimes = availableTimes;
        this.table = table;
    }

    // Getters and setters
    public List<Integer> getAvailableTimes() {
        return availableTimes;
    }

    public void setAvailableTimes(List<Integer> availableTimes) {
        this.availableTimes = availableTimes;
    }

    public Table getTable() {
        return table;
    }

    public void setTable(Table table) {
        this.table = table;
    }
}
