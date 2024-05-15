//package Repository.User;
//
//import Entity.User.ManagerEntity;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.stereotype.Repository;
//
//@Repository
//public interface ManagerRepository extends JpaRepository<ManagerEntity, Long> {
//    ManagerEntity findByUsername( String username);
//}
package Repository.User;

import Entity.User.ManagerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ManagerRepository extends JpaRepository<ManagerEntity, Long> {
    @Query("SELECT u FROM ManagerEntity u WHERE u.username = :username")
    ManagerEntity findByUsername(@Param("username") String username);
}
