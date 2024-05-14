package Repository.User;

import Entity.User.ManagerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ManagerRepository extends JpaRepository<ManagerEntity, Long> {
    ManagerEntity findByUsername( String username);
}
