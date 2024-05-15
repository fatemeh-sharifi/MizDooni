//package DAO.User;
//
//import Entity.User.ManagerEntity;
//import jakarta.persistence.EntityManager;
//import jakarta.persistence.PersistenceContext;
//import jakarta.transaction.Transactional;
//import org.springframework.stereotype.Repository;
//
//import java.util.List;
//
//@Repository
//@Transactional
//public class ManagerDAO {
//    @PersistenceContext
//    private EntityManager entityManager;
//
//    public void saveOrUpdate( ManagerEntity user) {
//        entityManager.merge(user);
//    }
//
//    public ManagerEntity getById(Long id) {
//        return entityManager.find(ManagerEntity.class, id);
//    }
//
//    public void delete(ManagerEntity user) {
//        entityManager.remove(user);
//    }
//
//    public List<ManagerEntity> getAllUsers() {
//        return entityManager.createQuery("SELECT u FROM ManagerEntity u", ManagerEntity.class).getResultList();
//    }
//
//    public ManagerEntity findByUsername( String username) {
//        return entityManager.createQuery("SELECT u FROM ManagerEntity u WHERE u.username = :username", ManagerEntity.class)
//                .setParameter("username", username)
//                .getSingleResult();
//    }
//}
