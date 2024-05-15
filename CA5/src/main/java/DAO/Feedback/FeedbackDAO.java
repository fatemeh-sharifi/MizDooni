package DAO.Feedback;

import Entity.Feedback.FeedbackEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Transactional
public class FeedbackDAO {

    @PersistenceContext
    private EntityManager entityManager;

    public void saveOrUpdate(FeedbackEntity feedback) {
        entityManager.merge(feedback);
    }

    public void delete(FeedbackEntity feedback) {
        entityManager.remove(feedback);
    }

    public List<FeedbackEntity> getAllFeedbacks() {
        return entityManager.createQuery("SELECT f FROM FeedbackEntity f", FeedbackEntity.class).getResultList();
    }

    public FeedbackEntity findById(long id) {
        return entityManager.find(FeedbackEntity.class, id);
    }

    // You can add more methods here as needed for specific queries or operations
}
