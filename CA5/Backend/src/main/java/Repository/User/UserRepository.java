package Repository.User;

import Entity.User.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    UserEntity findByUsername(String username);
    UserEntity findByUsernameAndPassword( String username, String password);

    boolean existsByUsername ( String username );
}
