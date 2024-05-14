package DAO.User;

import Entity.User.ClientEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Transactional
public class ClientDAO {
    @PersistenceContext
    private EntityManager entityManager;

    public void saveOrUpdate( ClientEntity user) {
        entityManager.merge(user);
    }

    public ClientEntity getById(Long id) {
        return entityManager.find(ClientEntity.class, id);
    }

    public void delete(ClientEntity user) {
        entityManager.remove(user);
    }

    public List<ClientEntity> getAllUsers() {
        return entityManager.createQuery("SELECT u FROM ClientEntity u", ClientEntity.class).getResultList();
    }

    public ClientEntity findByUsername( String username) {
        return entityManager.createQuery("SELECT u FROM ClientEntity u WHERE u.username = :username", ClientEntity.class)
                .setParameter("username", username)
                .getSingleResult();
    }
}
