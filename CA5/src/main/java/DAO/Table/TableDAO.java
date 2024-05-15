//package DAO.Table;
//
//import Entity.Table.TableEntity;
//import jakarta.persistence.EntityManager;
//import jakarta.persistence.PersistenceContext;
//import jakarta.transaction.Transactional;
//import org.springframework.stereotype.Repository;
//
//@Repository
//@Transactional
//public class TableDAO {
//    @PersistenceContext
//    private EntityManager entityManager;
//    public TableEntity save( TableEntity table) {
//        entityManager.merge(table);
//        return table;
//    }
//}
