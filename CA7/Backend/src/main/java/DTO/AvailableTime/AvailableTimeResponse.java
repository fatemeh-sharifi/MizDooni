package DTO.AvailableTime;

import Entity.Table.TableEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class AvailableTimeResponse {
    private List<Integer> availableTimes;
    private TableEntity availableTable;
}
