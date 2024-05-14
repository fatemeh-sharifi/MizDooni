package DAO.User;

import Entity.User.UserEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Transactional
public class UserDAO {
    @PersistenceContext
    private EntityManager entityManager;

    public void saveOrUpdate(UserEntity user) {
        entityManager.merge(user);
    }

    public UserEntity getById(Long id) {
        return entityManager.find(UserEntity.class, id);
    }

    public void delete(UserEntity user) {
        entityManager.remove(user);
    }

    public List<UserEntity> getAllUsers() {
        return entityManager.createQuery("SELECT u FROM UserEntity u", UserEntity.class).getResultList();
    }

    public UserEntity findByUsername( String username) {
        return entityManager.createQuery("SELECT u FROM UserEntity u WHERE u.username = :username", UserEntity.class)
                .setParameter("username", username)
                .getSingleResult();
    }
}
