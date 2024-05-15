package Routers;

import Entity.Table.TableEntity;
import Repository.Table.TableRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TableService {
    @Autowired
    private TableRepository tableRepository;
    @GetMapping("/tables")
    public ResponseEntity<List<TableEntity>> getTables() {
        List<TableEntity> tables = tableRepository.findAll();
        return ResponseEntity.ok(tables);
    }
}
