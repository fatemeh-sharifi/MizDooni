package Controller.Table;

import Entity.Table.TableEntity;
import Repository.Table.TableRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import co.elastic.apm.api.ElasticApm;
import co.elastic.apm.api.Scope;
import co.elastic.apm.api.Span;
import co.elastic.apm.api.Transaction;
@RestController
public class TableController {
    @Autowired
    private TableRepository tableRepository;
//    @GetMapping("/tables")
//    public ResponseEntity<List<TableEntity>> getTables() {
//        List<TableEntity> tables = tableRepository.findAll();
//        return ResponseEntity.ok(tables);
//    }
    @GetMapping("/tables")
    public ResponseEntity<List<TableEntity>> getTables() {
        Transaction transaction = ElasticApm.startTransaction();
        try (Scope txScope = transaction.activate()) {
            transaction.setName("HTTP GET /tables");
            transaction.setType(Transaction.TYPE_REQUEST);

            Span dbSpan = transaction.startSpan("db", "query", "findAllTables");
            try (Scope dbScope = dbSpan.activate()) {
                dbSpan.setName("Find All Tables");

                List<TableEntity> tables = tableRepository.findAll();
                dbSpan.end();

                return ResponseEntity.ok(tables);
            } catch (Exception e) {
                dbSpan.captureException(e);
                dbSpan.end();
                throw e;
            }
        } catch (Exception e) {
            transaction.captureException(e);
            return ResponseEntity.badRequest().body(null);
        } finally {
            transaction.end();
        }
    }
}
